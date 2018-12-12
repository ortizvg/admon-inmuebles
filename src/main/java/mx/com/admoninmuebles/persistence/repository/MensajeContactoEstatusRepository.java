package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.MensajeContactoEstatus;
import mx.com.admoninmuebles.persistence.model.Sector;

@Repository
public interface MensajeContactoEstatusRepository extends CrudRepository<MensajeContactoEstatus, Long>{
	
	Collection<MensajeContactoEstatus> findByIdioma(String idioma);
}
