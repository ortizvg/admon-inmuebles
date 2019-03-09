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
    
//    Collection<Ticket> findByUsuarioAsignadoIdAndEstatusTicketName(Long id, String estatusTicketName);
    
    @Query(value = "SELECT count(t.id_ticket) FROM gescopls.inmuebles_socios ist\r\n" + 
    		"join gescopls.tickets t on ist.socios_id_usuario = t.id_usuario\n" +  
    		"join gescopls.estatus_tickets et on t.id_estatus_ticket_fk = et.id_estatus_ticket\r\n" + 
    		"join gescopls.tipos_ticket tt on t.id_tipo_ticket_fk = tt.id_id_tipo_ticket\r\n" + 
    		"where ist.Inmueble_id_inmueble = ?1\r\n" + 
    		"and tt.name = ?2\r\n" + 
    		"and et.name = ?3", 
			nativeQuery = true)
	Long countByInmuebleIdAndTipoTicketNameAndEstatusTicketName(Long inmuebleId, String tipoTicketName, String estatusTicketName);
    
    @Query(value = "SELECT t.* FROM gescopls.inmuebles_socios ist\r\n" + 
    		"join gescopls.tickets t on ist.socios_id_usuario = t.id_usuario\n" +  
    		"join gescopls.estatus_tickets et on t.id_estatus_ticket_fk = et.id_estatus_ticket\r\n" + 
    		"join gescopls.tipos_ticket tt on t.id_tipo_ticket_fk = tt.id_id_tipo_ticket\r\n" + 
    		"where ist.Inmueble_id_inmueble = ?1\r\n" + 
    		"and tt.name = ?2\r\n" + 
    		"and et.name = ?3", 
			nativeQuery = true)
    Collection<Ticket> findByInmuebleIdAndTipoTicketNameAndEstatusTicketName(Long inmuebleId, String tipoTicketName, String estatusTicketName);
    
    
    @Query(value = "SELECT count(t.id_ticket) FROM gescopls.inmuebles_socios ist\n" + 
    		"join gescopls.tickets t on ist.socios_id_usuario = t.id_usuario\n" + 
    		"where ist.Inmueble_id_inmueble = ?1", 
			nativeQuery = true)
	Long countByInmuebleId(Long id);
    
}
