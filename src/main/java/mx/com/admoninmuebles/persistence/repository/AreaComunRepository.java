package mx.com.admoninmuebles.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.AreaComun;

@Repository
public interface AreaComunRepository extends CrudRepository<AreaComun, Long> {

}
