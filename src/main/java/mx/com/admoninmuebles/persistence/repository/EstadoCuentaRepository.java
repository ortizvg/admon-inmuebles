package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.EstadoCuenta;

public interface EstadoCuentaRepository extends CrudRepository<EstadoCuenta, Long>{
	
	Collection<EstadoCuenta> findBySocioId( Long socioId );
	
	Optional<EstadoCuenta> findFirst1BySocioIdOrderByFechaModificacionDesc( Long socioId );
	
    @Query(value = "select ec.* from gescopls.inmuebles i\r\n" + 
    		"inner join gescopls.inmuebles_socios inmsoc on i.id_inmueble = inmsoc.inmueble_id_inmueble\r\n" + 
    		"inner join gescopls.estados_cuenta ec on inmsoc.socios_id_usuario = ec.id_socio_fk\r\n" + 
    		"where i.id_inmueble = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<EstadoCuenta> findByInmuebleId( Long inmuebleId );
	
    @Query(value = "select ec.* from gescopls.usuarios contador \r\n" + 
    		"inner join gescopls.inmuebles i on contador.id_usuario = i.id_contador_fk\r\n" + 
    		"inner join gescopls.inmuebles_socios inmsoc on i.id_inmueble = inmsoc.inmueble_id_inmueble\r\n" + 
    		"inner join gescopls.estados_cuenta ec on inmsoc.socios_id_usuario = ec.id_socio_fk\r\n" + 
    		"where contador.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<EstadoCuenta> findByContadorId( Long contadorId );
    
    @Query(value = "select ec.* from gescopls.usuarios ab\r\n" + 
    		"inner join gescopls.inmuebles i on ab.id_usuario = i.id_admin_bi_fk\r\n" + 
    		"inner join gescopls.inmuebles_socios inmsoc on i.id_inmueble = inmsoc.inmueble_id_inmueble \r\n" + 
    		"inner join gescopls.estados_cuenta ec on inmsoc.socios_id_usuario = ec.id_socio_fk\r\n" + 
    		"where ab.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<EstadoCuenta> findByAdminBiId( Long adminBiId );
    
    @Query(value = "select ec.* from gescopls.usuarios az \r\n" + 
    		"inner join gescopls.zonas z on az.id_usuario = z.id_admin_zona_fk\r\n" + 
    		"inner join gescopls.asentamientos a on  z.codigo = a.id_zona_fk\r\n" + 
    		"inner join gescopls.direcciones d on a.id_asentamiento = d.id_asentamiento_fk\r\n" + 
    		"inner join gescopls.inmuebles i on d.id_direccion = i.id_direccion_fk\r\n" + 
    		"inner join gescopls.inmuebles_socios inmsoc on i.id_inmueble = inmsoc.inmueble_id_inmueble \r\n" + 
    		"inner join gescopls.estados_cuenta ec on inmsoc.socios_id_usuario = ec.id_socio_fk\r\n" + 
    		"where az.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<EstadoCuenta> findByAdminZonaId( Long adminZonaId );
	
	
	

}
