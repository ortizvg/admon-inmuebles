package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.TipoPago;

@Repository
public interface TipoPagoRepository extends CrudRepository<TipoPago, Long>  {
	
	TipoPago findByNameAndLang(final String name,final String lang);
	Collection<TipoPago> findAllByLang(final String lang);
	
	Collection<TipoPago> findByActivo(final boolean activo);
	TipoPago findByNameAndLangAndActivo(final String name, final String lang, final boolean activo);
	Collection<TipoPago> findAllByLangAndActivo(String lang, final boolean activo);
}
