package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.CuotaDepartamento;

public interface CuotaDepartamentoRepository extends CrudRepository<CuotaDepartamento, Long> {
	
	Collection<CuotaDepartamento> findByInmuebleId( Long inmuebleId );
	
    @Query(value = "select cd.* from usuarios c \r\n" + 
    		"inner join inmuebles i on c.id_usuario = i.id_contador_fk\r\n" + 
    		"inner join cuotas_departamentos cd on i.id_inmueble = cd.id_inmueble_fk\r\n" + 
    		" where c.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<CuotaDepartamento> findByContadorId( Long contadorId );
	
	

}
