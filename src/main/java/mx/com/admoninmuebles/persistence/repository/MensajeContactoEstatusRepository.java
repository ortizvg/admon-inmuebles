package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.MensajeContactoEstatus;

@Repository
public interface MensajeContactoEstatusRepository extends CrudRepository<MensajeContactoEstatus, Long>{
	
	Collection<MensajeContactoEstatus> findByIdioma(final String idioma);
	
	Collection<MensajeContactoEstatus> findByActivo(final boolean activo);
	
	Collection<MensajeContactoEstatus> findByIdiomaAndActivo(final String idioma, final boolean activo);
}
