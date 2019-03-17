package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.Inmueble;
import mx.com.admoninmuebles.persistence.model.Zona;

@Repository
public interface InmuebleRepository extends CrudRepository<Inmueble, Long> {
	
	Collection<Inmueble> findByAdminBiId(Long id);
	
	Collection<Inmueble> findByContadorId(Long id);
	
	long countByContadorId(Long id);
	
	Collection<Inmueble> findByDireccionAsentamientoId(Long id);
	
	Collection<Inmueble> findBySociosId(Long id);
	
	Collection<Inmueble> findByDireccionAsentamientoZonaCodigo(String codigo);
	
	Collection<Inmueble> findByDireccionAsentamientoZonaIn(Collection<Zona> zonas);
	
    @Query(value = "select i.* from gescopls.inmuebles i\r\n" + 
    		"join gescopls.inmuebles_socios inso on i.id_inmueble = inso.inmueble_id_inmueble\r\n" + 
    		"where inso.socios_id_usuario =  ?1", 
			nativeQuery = true)
	Inmueble findBySocioId(Long id);
    
    @Query(value = "select inm.* from gescopls.tickets t\r\n" + 
    		"join gescopls.usuarios prov on t.id_usuario_asignado_fk = prov.id_usuario\r\n" + 
    		"join gescopls.usuarios soc on t.id_usuario_creador_fk = soc.id_usuario\r\n" + 
    		"join gescopls.inmuebles_socios inso on soc.id_usuario = inso.socios_id_usuario\r\n" + 
    		"join gescopls.inmuebles inm on inso.Inmueble_id_inmueble = inm.id_inmueble\r\n" + 
    		"where prov.id_usuario = ?1", 
			nativeQuery = true)
    Collection<Inmueble> findInmueblesByTicketAsignedToProveedorId( final Long idProveedor);
	

}