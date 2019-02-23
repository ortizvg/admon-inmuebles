package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.ReporteMensual;
import mx.com.admoninmuebles.persistence.model.TipoReporteMensual;

public interface ReporteMensualRepository extends CrudRepository<ReporteMensual, Long> {
	
	Collection<ReporteMensual> findByInmuebleId( Long inmuebleId );
	
	Optional<ReporteMensual> findByInmuebleIdAndAnioAndMesIdAndTipoReporteMensualId(Long inmuebleId, Integer anio, Long mesId, Long reporteMensualId);
	
	Collection<ReporteMensual> findByInmuebleIdAndAnioAndMesIdAndTipoReporteMensualIn( Long inmuebleId, Integer anio, Long mesId, Collection<TipoReporteMensual> tiposReporteMensual);
	
	Collection<ReporteMensual> findByInmuebleContadorIdAndAnioAndMesIdAndTipoReporteMensualIn( Long contadorId, Integer anio, Long mesId, Collection<TipoReporteMensual> tiposReporteMensual);
	
	
    @Query(value = "select rm.* from usuarios c \r\n" + 
    		"inner join inmuebles i on c.id_usuario = i.id_contador_fk\r\n" + 
    		"inner join reportes_mensuales rm on i.id_inmueble = rm.id_inmueble_fk\r\n" + 
    		" where c.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<ReporteMensual> findByContadorId( Long contadorId );
    
	Collection<ReporteMensual> findFirst5ByInmuebleIdOrderByIdDesc( Long inmuebleId );
	
    @Query(value = "select rm.* from gescopls.usuarios ab\r\n" + 
    		"inner join gescopls.inmuebles i on ab.id_usuario = i.id_admin_bi_fk\r\n" + 
    		"inner join gescopls.reportes_mensuales rm on i.id_inmueble = rm.id_inmueble_fk\r\n" + 
    		"where ab.id_usuario =  ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<ReporteMensual> findByAdminBiId( Long adminBiId );
    
    
    @Query(value = "select rm.* from gescopls.usuarios az \r\n" + 
    		"inner join gescopls.zonas z on az.id_usuario = z.id_admin_zona_fk\r\n" + 
    		"inner join gescopls.asentamientos a on  z.codigo = a.id_zona_fk\r\n" + 
    		"inner join gescopls.direcciones d on a.id_asentamiento = d.id_asentamiento_fk\r\n" + 
    		"inner join gescopls.inmuebles i on d.id_direccion = i.id_direccion_fk\r\n" + 
    		"inner join gescopls.reportes_mensuales rm on i.id_inmueble = rm.id_inmueble_fk\r\n" + 
    		"where az.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<ReporteMensual> findByAdminZonaId( Long adminZonaId );

}
