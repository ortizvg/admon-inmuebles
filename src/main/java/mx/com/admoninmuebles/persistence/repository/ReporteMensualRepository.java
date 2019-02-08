package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.EstadoCuenta;
import mx.com.admoninmuebles.persistence.model.ReporteMensual;

public interface ReporteMensualRepository extends CrudRepository<ReporteMensual, Long> {
	
	Collection<ReporteMensual> findByInmuebleId( Long inmuebleId );
	
	
    @Query(value = "select rm.* from usuarios c \r\n" + 
    		"inner join inmuebles i on c.id_usuario = i.id_contador_fk\r\n" + 
    		"inner join reportes_mensuales rm on i.id_inmueble = rm.id_inmueble_fk\r\n" + 
    		" where c.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<ReporteMensual> findByContadorId( Long contadorId );
    
	Collection<ReporteMensual> findFirst5ByInmuebleIdOrderByIdDesc( Long inmuebleId );

}
