package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.Reglamento;

public interface ReglamentoRepository  extends CrudRepository<Reglamento, Long> {
	
	Collection<Reglamento> findByInmuebleId( Long inmuebleId );
	
    @Query(value = "select r.* from usuarios c \r\n" + 
    		"inner join inmuebles i on c.id_usuario = i.id_contador_fk\r\n" + 
    		"inner join reglamentos r on i.id_inmueble = r.id_inmueble_fk\r\n" + 
    		" where c.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<Reglamento> findByContadorId( Long contadorId );

}
