package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.Ticket;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
	
    Collection<Ticket> findByUsuarioCreadorId(Long id);

    Collection<Ticket> findByUsuarioAsignadoId(Long id);
    
    Collection<Ticket> findByUsuarioAsignadoIdAndEstatus(Long id, String estatusTicketName);
    
    @Query(value = "SELECT count(t.id_ticket) FROM gescopls.inmuebles_socios ist\r\n" + 
    		"join gescopls.tickets t on ist.socios_id_usuario = t.id_usuario_creador_fk\r\n" + 
    		"join gescopls.tipos_ticket tt on t.id_tipo_ticket_fk = tt.id_tipo_ticket\r\n" + 
    		"where ist.Inmueble_id_inmueble = ?1\r\n" + 
    		"and tt.id_tipo_ticket = ?2\r\n" + 
    		"and t.estatus = ?3", 
			nativeQuery = true)
	Long countByInmuebleIdAndTipoTicketIdAndEstatusTicketName(Long inmuebleId, Long tipoTicketId, String estatusTicketName);
    
    @Query(value = "SELECT t.* FROM gescopls.inmuebles_socios ist\r\n" + 
    		"join gescopls.tickets t on ist.socios_id_usuario = t.id_usuario_creador_fk\r\n" + 
    		"join gescopls.tipos_ticket tt on t.id_tipo_ticket_fk = tt.id_tipo_ticket\r\n" + 
    		"where ist.Inmueble_id_inmueble = ?1\r\n" + 
    		"and tt.id_tipo_ticket = ?2\r\n" + 
    		"and t.estatus = ?3", 
			nativeQuery = true)
    Collection<Ticket> findByInmuebleIdAndTipoTicketNameAndEstatusTicketName(Long inmuebleId, Long tipoTicketId, String estatusTicketName);
    
    @Query(value = "SELECT count(t.id_ticket) FROM gescopls.inmuebles_socios ist\r\n" + 
    		"join gescopls.tickets t on ist.socios_id_usuario = t.id_usuario_creador_fk\r\n" + 
    		"join gescopls.tipos_ticket tt on t.id_tipo_ticket_fk = tt.id_tipo_ticket\r\n" + 
    		"where ist.Inmueble_id_inmueble = ?1\r\n" + 
    		"and t.estatus = ?2", 
			nativeQuery = true)
	Long countByInmuebleIdAndEstatusTicketName(Long inmuebleId, String estatusTicketName);
    
    @Query(value = "SELECT t.* FROM gescopls.inmuebles_socios ist\r\n" + 
    		"join gescopls.tickets t on ist.socios_id_usuario = t.id_usuario_creador_fk\r\n" + 
    		"where ist.Inmueble_id_inmueble = ?1\r\n" + 
    		"and t.estatus = ?2", 
			nativeQuery = true)
    Collection<Ticket> findByInmuebleIdAndEstatusTicketName(Long inmuebleId, String tipoTicketName, String estatusTicketName);
    
    
    @Query(value = "SELECT count(t.id_ticket) FROM gescopls.inmuebles_socios ist\n" + 
    		"join gescopls.tickets t on ist.socios_id_usuario = t.id_usuario_creador_fk\n" + 
    		"where ist.Inmueble_id_inmueble = ?1", 
			nativeQuery = true)
	Long countByInmuebleId(Long id);
    
    @Query(value = "SELECT count(t.id_ticket) FROM gescopls.inmuebles_socios ist\r\n" + 
    		"join gescopls.tickets t on ist.socios_id_usuario = t.id_usuario_creador_fk\r\n" + 
    		"join gescopls.tipos_ticket tt on t.id_tipo_ticket_fk = tt.id_tipo_ticket\r\n" + 
    		"where ist.Inmueble_id_inmueble = ?1\r\n" + 
    		"and tt.id_tipo_ticket = ?2",
			nativeQuery = true)
	Long countByInmuebleIdAndTipoTicketId(Long id,  Long tipoTicketId);
    
    @Query(value = "select t.* from gescopls.usuarios u  \r\n" + 
    		"inner join gescopls.inmuebles i on u.id_usuario = i.id_admin_bi_fk \r\n" + 
    		"inner join gescopls.inmuebles_socios ist on i.id_inmueble = ist.Inmueble_id_inmueble\r\n" + 
    		"join gescopls.tickets t on ist.socios_id_usuario = t.id_usuario_creador_fk\r\n" + 
    		"where u.id_usuario =  ?1 order by t.id_ticket desc", 
			nativeQuery = true)
    Collection<Ticket> findByAdminBiId(Long id);
    
    @Query(value = "select t.* from gescopls.usuarios az\r\n" + 
    		"inner join gescopls.zonas z on az.id_usuario = z.id_admin_zona_fk\r\n" + 
    		"inner join gescopls.asentamientos a on  z.codigo = a.id_zona_fk\r\n" + 
    		"inner join gescopls.direcciones d on a.id_asentamiento = d.id_asentamiento_fk\r\n" + 
    		"inner join gescopls.inmuebles i on d.id_direccion = i.id_direccion_fk\r\n" + 
    		"inner join gescopls.inmuebles_socios ist on i.id_inmueble = ist.Inmueble_id_inmueble\r\n" + 
    		"join gescopls.tickets t on ist.socios_id_usuario = t.id_usuario_creador_fk\r\n" + 
    		"where az.id_usuario = ?1 order by t.id_ticket desc", 
			nativeQuery = true)
    Collection<Ticket> findByAdminZonaId(Long id);
    
    
}
