package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.TipoPagoBancario;

@Repository
public interface TipoPagoBancarioRepository extends CrudRepository<TipoPagoBancario, Long>  {
	
	TipoPagoBancario findByNameAndLang(final String name, final String lang);
	
	Collection<TipoPagoBancario> findByActivo(final boolean activo);
	TipoPagoBancario findByNameAndLangAndActivo(final String name, final String lang, final boolean activo);
	
}
