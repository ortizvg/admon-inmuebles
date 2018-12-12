package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.Inmueble;

@Repository
public interface InmuebleRepository extends CrudRepository<Inmueble, Long> {
	
	Collection<Inmueble> findByAdminBiId(Long id);
	
	Collection<Inmueble> findByDireccionAsentamientoId(Long id);
	
	Collection<Inmueble> findBySociosId(Long id);
}
