package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.Rol;
import mx.com.admoninmuebles.persistence.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
	
//	boolean existByUsername( String userName );

    Optional<Usuario> findByUsername(String username);
    
    Collection<Usuario> findByDireccionAsentamientoZonaCodigo(String codigoZona);

    Optional<Usuario> findByCorreo(String correo);

    Collection<Usuario> findByRolesNombre(String nombre);
    
    Collection<Usuario> findByRolesNombreAndActivo(String nombre, boolean activo);

    Collection<Usuario> findByRolesNombreAndAreasServicioId(String nombre, Long id);
    
    Collection<Usuario> findByRolesIn(List<Rol> roles);
    
    @Query(value = "select s.* from gescopls.usuarios s where s.id_usuario in (SELECT ims.socios_id_usuario FROM gescopls.zonas z\n" + 
			"inner join gescopls.asentamientos a on z.codigo = a.id_zona_fk\n" + 
			"inner join gescopls.direcciones d on a.id_asentamiento = d.id_asentamiento_fk\n" + 
			"inner join gescopls.inmuebles i on d.id_direccion = i.id_direccion_fk\n" + 
			"inner join gescopls.inmuebles_socios ims on i.id_inmueble = ims.Inmueble_id_inmueble\n" + 
			"where z.codigo = ?1)", 
			nativeQuery = true)
    Collection<Usuario> findSociosByZonaCodigo(String zonaCodigo);
    
    @Query(value = "select s.* from gescopls.usuarios s\r\n" + 
    		"where s.id_usuario in (SELECT ims.socios_id_usuario FROM gescopls.usuarios u\r\n" + 
    		"inner join gescopls.inmuebles i on i.id_admin_bi_fk = u.id_usuario\r\n" + 
    		"inner join gescopls.inmuebles_socios ims on i.id_inmueble = ims.Inmueble_id_inmueble\r\n" + 
    		"where u.id_usuario =  ?1)", 
			nativeQuery = true)
    Collection<Usuario> findSociosByAdminBiId(Long id);
    
    @Query(value = "select s.* from gescopls.usuarios az\r\n" + 
    		"	 inner join gescopls.zonas z on az.id_usuario = z.id_admin_zona_fk\r\n" + 
    		"    inner join gescopls.asentamientos a on  z.codigo = a.id_zona_fk\r\n" + 
    		"    inner join gescopls.direcciones d on a.id_asentamiento = d.id_asentamiento_fk\r\n" + 
    		"    inner join gescopls.inmuebles i on d.id_direccion = i.id_direccion_fk\r\n" + 
    		"    inner join gescopls.inmuebles_socios inso on i.id_inmueble = inso.inmueble_id_inmueble\r\n" + 
    		"    inner join gescopls.usuarios s on inso.socios_id_usuario = s.id_usuario\r\n" + 
    		"    inner join gescopls.pagos p on s.id_usuario = p.id_usuario\r\n" + 
    		"    where az.id_usuario = ?1", 
			nativeQuery = true)
    Collection<Usuario> findSociosByAdminZonaId(Long id);
    
    @Query(value = "select s.* from gescopls.usuarios s where s.id_usuario in (SELECT ims.socios_id_usuario FROM gescopls.zonas z\n" + 
			"inner join gescopls.asentamientos a on z.codigo = a.id_zona_fk\n" + 
			"inner join gescopls.direcciones d on a.id_asentamiento = d.id_asentamiento_fk\n" + 
			"inner join gescopls.inmuebles i on d.id_direccion = i.id_direccion_fk\n" + 
			"inner join gescopls.inmuebles_socios ims on i.id_inmueble = ims.Inmueble_id_inmueble\n" + 
			"where z.codigo = ?1)", 
			nativeQuery = true)
    Collection<Usuario> findAdministradoresBiByAdminZona(Long adminZonaId);
    
}
