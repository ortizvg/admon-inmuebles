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
	
	
	

}
