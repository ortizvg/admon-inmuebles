package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.Sector;

public interface SectorRepository extends CrudRepository<Sector, Long>{
	
	Collection<Sector> findByIdioma(String idioma);

}
