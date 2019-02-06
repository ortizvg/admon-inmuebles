package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.ReporteMensual;

public interface ReporteMensualRepository extends CrudRepository<ReporteMensual, Long> {
	
	Collection<ReporteMensual> findByInmuebleId( Long inmuebleId );

}
