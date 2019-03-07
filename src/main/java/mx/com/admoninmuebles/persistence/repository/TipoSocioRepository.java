package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.TipoSocio;

@Repository
public interface TipoSocioRepository extends CrudRepository<TipoSocio, Long> {
	
	TipoSocio findByNameAndLang(final String name,final String lang);
	Collection<TipoSocio> findByLang(final String lang);
	
	Collection<TipoSocio> findByActivo(final boolean activo);
	TipoSocio findByNameAndLangAndActivo(final String name,final String lang, final boolean activo);
	Collection<TipoSocio> findByLangAndActivo(final String lang, final boolean activo);

}
