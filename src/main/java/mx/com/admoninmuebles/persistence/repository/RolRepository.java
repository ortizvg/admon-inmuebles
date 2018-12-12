package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.Rol;

@Repository
public interface RolRepository extends CrudRepository<Rol, Long> {
	
    Optional<Rol> findByNombre(String nombre);
    
    @Query( "select o from Rol o where nombre in :nombres" )
    Collection<Rol> findByNombres(@Param("nombres")List<String> nombres);

}
