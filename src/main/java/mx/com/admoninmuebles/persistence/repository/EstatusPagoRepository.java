package mx.com.admoninmuebles.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.EstatusPago;

@Repository
public interface EstatusPagoRepository   extends CrudRepository<EstatusPago, Long> {
	
	EstatusPago findByNameAndLang(final String name,final String lang);
	
	EstatusPago findByNameAndLangAndActivo(final String name,final String lang, final boolean activo);

}
