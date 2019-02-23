package mx.com.admoninmuebles.persistence.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.Notificacion;

@Repository
public interface NotificacionRepository extends CrudRepository<Notificacion, Long> {
	Collection<Notificacion> findByInmuebleId(Long id);
	Collection<Notificacion> findByInmuebleIdAndFechaExposicionInicialBeforeAndFechaExposicionFinalAfter(Long id, Date today, Date todayf);
	Collection<Notificacion> findByInmuebleIdAndFechaExposicionInicialLessThanEqualAndFechaExposicionFinalGreaterThanEqual(Long id, LocalDate today, LocalDate todayf);
	Collection<Notificacion> findByUsuarioIdAndFechaExposicionInicialLessThanEqualAndFechaExposicionFinalGreaterThanEqual(Long idUsuario, LocalDate today, LocalDate todayf);
	Collection<Notificacion> findByInmuebleIdOrUsuarioIdAndFechaExposicionInicialLessThanEqualAndFechaExposicionFinalGreaterThanEqual(Long idInmueble, Long idUsuario, LocalDate today, LocalDate todayf);
	Collection<Notificacion> findByInmuebleIdInAndFechaExposicionInicialLessThanEqualAndFechaExposicionFinalGreaterThanEqual(List<Long> idsInmuebles, LocalDate today, LocalDate todayf);

    @Query(value = "select n.* from gescopls.notificaciones n\n" + 
    		"inner join gescopls.inmuebles i on n.id_inmueble = i.id_inmueble\n" + 
    		"inner join gescopls.direcciones d on i.id_direccion_fk = d.id_direccion\n" + 
    		"inner join gescopls.asentamientos a on d.id_asentamiento_fk = a.id_asentamiento\n" + 
    		"inner join gescopls.zonas z on a.id_zona_fk = z.codigo\n" + 
    		"where z.codigo =  ?1", 
			nativeQuery = true)
	Collection<Notificacion> findByZonaId(String id);
    
    Collection<Notificacion> findByInmuebleAdminBiId(Long id);
    
    @Query(value = "select n.* from gescopls.usuarios admbi \r\n" + 
    		"inner join gescopls.inmuebles i on admbi.id_usuario = i.id_admin_bi_fk\r\n" + 
    		"inner join gescopls.inmuebles_socios inso on i.id_inmueble = inso.inmueble_id_inmueble\r\n" + 
    		"inner join gescopls.usuarios s on inso.socios_id_usuario = s.id_usuario \r\n" + 
    		"inner join gescopls.notificaciones n on s.id_usuario = n.id_usuario or i.id_inmueble = n.id_inmueble\r\n" + 
    		"where admbi.id_usuario = ?1", 
			nativeQuery = true)
    Collection<Notificacion> findByAdminBiId(Long id);
    
    @Query(value = "select n.* from gescopls.usuarios az\r\n" + 
    		"inner join gescopls.zonas z on az.id_usuario = z.id_admin_zona_fk\r\n" + 
    		"inner join gescopls.asentamientos a on  z.codigo = a.id_zona_fk\r\n" + 
    		"inner join gescopls.direcciones d on a.id_asentamiento = d.id_asentamiento_fk \r\n" + 
    		"inner join gescopls.inmuebles i on d.id_direccion = i.id_direccion_fk\r\n" + 
    		"inner join gescopls.inmuebles_socios inso on i.id_inmueble = inso.inmueble_id_inmueble\r\n" + 
    		"inner join gescopls.usuarios s on inso.socios_id_usuario = s.id_usuario \r\n" + 
    		"inner join gescopls.notificaciones n on s.id_usuario = n.id_usuario or i.id_inmueble = n.id_inmueble\r\n" + 
    		"where az.id_usuario =  ?1", 
			nativeQuery = true)
    Collection<Notificacion> findByAdminZonaId(Long id);
}
