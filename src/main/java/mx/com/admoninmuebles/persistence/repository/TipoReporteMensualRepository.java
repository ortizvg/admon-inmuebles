package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.TipoReporteMensual;

public interface TipoReporteMensualRepository extends CrudRepository<TipoReporteMensual, Long>  {
	
	Collection<TipoReporteMensual> findByLang(final String lang);
	
	Collection<TipoReporteMensual> findByActivo(final boolean activo);
	Collection<TipoReporteMensual> findByLangAndActivo(final String lang, final boolean activo);

}
