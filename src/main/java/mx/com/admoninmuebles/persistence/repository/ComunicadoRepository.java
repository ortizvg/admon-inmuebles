package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.Comunicado;
import mx.com.admoninmuebles.persistence.model.Reglamento;

public interface ComunicadoRepository extends CrudRepository<Comunicado, Long> {
	
	Collection<Comunicado> findByInmuebleId( Long inmuebleId );

}
