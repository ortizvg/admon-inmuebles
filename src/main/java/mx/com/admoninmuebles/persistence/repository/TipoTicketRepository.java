package mx.com.admoninmuebles.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.TipoTicket;

@Repository
public interface TipoTicketRepository extends CrudRepository<TipoTicket, Long> {

	Optional<TipoTicket> findByNombre(final String nombre);
}
