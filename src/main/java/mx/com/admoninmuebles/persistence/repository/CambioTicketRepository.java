package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.CambioTicket;

@Repository
public interface CambioTicketRepository extends CrudRepository<CambioTicket, String> {

	Collection<CambioTicket> findByTicketId(Long id);

	Optional<CambioTicket> findById(long id);

}
