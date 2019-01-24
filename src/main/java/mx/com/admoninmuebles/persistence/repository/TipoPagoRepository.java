package mx.com.admoninmuebles.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.TipoPago;

@Repository
public interface TipoPagoRepository extends CrudRepository<TipoPago, Long>  {
	
	TipoPago findByNameAndLang(final String name,final String lang);
}
