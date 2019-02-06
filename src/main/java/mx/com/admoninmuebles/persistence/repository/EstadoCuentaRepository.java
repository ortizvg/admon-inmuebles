package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.EstadoCuenta;

public interface EstadoCuentaRepository extends CrudRepository<EstadoCuenta, Long>{
	
	Collection<EstadoCuenta> findBySocioId( Long socioId );
	
	Optional<EstadoCuenta> findFirst1BySocioIdOrderByFechaModificacionDesc( Long socioId );

}
