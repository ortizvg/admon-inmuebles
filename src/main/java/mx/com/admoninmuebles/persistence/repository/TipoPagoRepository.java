package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.TipoPago;

@Repository
public interface TipoPagoRepository extends CrudRepository<TipoPago, Long>  {
	
	TipoPago findByNameAndLangg(final String name,final String langg);
	Collection<TipoPago> findAllByLangg(final String langg);
	
	Collection<TipoPago> findByActivo(final boolean activo);
	TipoPago findByNameAndLanggAndActivo(final String name, final String langg, final boolean activo);
	Collection<TipoPago> findAllByLanggAndActivo(String langg, final boolean activo);
}
