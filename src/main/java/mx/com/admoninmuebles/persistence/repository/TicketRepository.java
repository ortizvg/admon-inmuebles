package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.Ticket;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Collection<Ticket> findByUsuarioCreadorId(Long id);

    Collection<Ticket> findByUsuarioAsignadoId(Long id);
    
}
