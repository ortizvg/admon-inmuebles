package mx.com.admoninmuebles.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.Privilegio;

@Repository
public interface PrivilegioRepository extends CrudRepository<Privilegio, String> {
    Optional<Privilegio> findByNombre(String nombre);
}
