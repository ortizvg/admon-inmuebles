package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.Asentamiento;

@Repository
public interface AsentamientoRepository extends CrudRepository<Asentamiento, Long> {
    Collection<Asentamiento> findByZonaIsNotNull();

    Collection<Asentamiento> findBycodigoPostal(String codigoPostal);
    
    Collection<Asentamiento> findByZonaCodigo(String codigo);
    
    Collection<Asentamiento> findBycodigoPostalAndZonaCodigo(String codigoPostal, String codigo);
}
