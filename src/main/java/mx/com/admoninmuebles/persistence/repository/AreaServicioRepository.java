package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.AreaServicio;

@Repository
public interface AreaServicioRepository extends CrudRepository<AreaServicio, Long>{
	
	Collection<AreaServicio> findByActivo( final boolean activo);
	boolean existsByNombre( final String nombre);

}
