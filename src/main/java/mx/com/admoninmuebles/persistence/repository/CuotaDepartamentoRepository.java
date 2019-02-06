package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.CuotaDepartamento;

public interface CuotaDepartamentoRepository extends CrudRepository<CuotaDepartamento, Long> {
	
	Collection<CuotaDepartamento> findBySocioId( Long socioId );
	
	Optional<CuotaDepartamento> findFirst1BySocioIdOrderByFechaModificacionDesc( Long socioId );
	
	

}
