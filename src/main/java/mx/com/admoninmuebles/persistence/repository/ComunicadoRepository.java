package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.Comunicado;

public interface ComunicadoRepository extends CrudRepository<Comunicado, Long> {
	
	Collection<Comunicado> findByInmuebleId( Long inmuebleId );
	
    @Query(value = "select co.* from usuarios c \r\n" + 
    		"inner join inmuebles i on c.id_usuario = i.id_contador_fk\r\n" + 
    		"inner join comunicados co on i.id_inmueble = co.id_inmueble_fk\r\n" + 
    		" where c.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<Comunicado> findByContadorId( Long contadorId );

}
