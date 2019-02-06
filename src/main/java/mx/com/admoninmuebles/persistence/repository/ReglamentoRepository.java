package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.Reglamento;

public interface ReglamentoRepository  extends CrudRepository<Reglamento, Long> {
	
	Collection<Reglamento> findByInmuebleId( Long inmuebleId );

}
