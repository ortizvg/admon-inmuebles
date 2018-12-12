package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.PreguntaFrecuente;

@Repository
public interface PreguntaFrecuenteRepository extends CrudRepository<PreguntaFrecuente, Long> {
	
	Collection<PreguntaFrecuente> findByIdioma(String idioma);
}
