package mx.com.admoninmuebles.persistence.model;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

public interface MesRepository extends CrudRepository<Mes, Long> {
	
	Collection<Mes> findByLang(String lang);

}
