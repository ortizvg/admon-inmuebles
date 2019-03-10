package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.Pago;

@Repository
public interface PagoRepository extends CrudRepository<Pago, Long>  {
	
	Collection<Pago> findByUsuarioId(Long id);
	
	Collection<Pago> findByUsuarioIdAndEstatusPagoNameOrderByUsuarioId( Long id, String estatusPagoName );
	
	Collection<Pago> findByEstatusPagoId(Long idEstatusPago);
	
	Collection<Pago> findByTipoPagoIdAndEstatusPagoId(Long idTipopago, Long idEstatusPago);
	
    @Query(value = "SELECT p.* FROM gescopls.inmuebles_socios ist\n" + 
    		"join gescopls.pagos p on ist.socios_id_usuario = p.id_usuario\n" + 
    		"where ist.Inmueble_id_inmueble = ?1", 
			nativeQuery = true)
	Collection<Pago> findByInmuebleId(Long id);
    
    @Query(value = "SELECT p.* FROM gescopls.inmuebles_socios ist\r\n" + 
    		"join gescopls.pagos p on ist.socios_id_usuario = p.id_usuario\r\n" + 
    		"join gescopls.estatus_pagos ep on p.id_estatus_pago = ep.id_estatus_pago\r\n" + 
    		"where ist.Inmueble_id_inmueble = ?1\r\n" + 
    		"and ep.name = ?2", 
			nativeQuery = true)
	Collection<Pago> findByInmuebleIdAndEstatusPagoName(Long inmuebleId, String estatusPagoName);
    
    @Query(value = "SELECT p.* FROM gescopls.inmuebles_socios ist\r\n" + 
    		"join gescopls.pagos p on ist.socios_id_usuario = p.id_usuario\r\n" + 
    		"join gescopls.estatus_pagos ep on p.id_estatus_pago = ep.id_estatus_pago\r\n" + 
    		"join gescopls.tipos_pagos tp on p.id_tipo_pago = tp.id_tipo_pago\r\n" + 
    		"where ist.Inmueble_id_inmueble = ?1\r\n" + 
    		"and ep.name = ?2\r\n" +
    		"and tp.name = ?3", 
			nativeQuery = true)
	Collection<Pago> findByInmuebleIdAndEstatusPagoNameAndTipoPagoName(Long inmuebleId, String estatusPagoName, String tipoPagoName);
    
    @Query(value = "SELECT count(p.id_pago) FROM gescopls.inmuebles_socios ist\r\n" + 
    		"join gescopls.pagos p on ist.socios_id_usuario = p.id_usuario\r\n" + 
    		"join gescopls.estatus_pagos ep on p.id_estatus_pago = ep.id_estatus_pago\r\n" + 
    		"where ist.Inmueble_id_inmueble = ?1\r\n" + 
    		"and ep.name = ?2", 
			nativeQuery = true)
	Long countByInmuebleIdAndEstatusPagoName(Long inmuebleId, String estatusPagoName);
    
    @Query(value = "SELECT count(p.id_pago) FROM gescopls.inmuebles_socios ist\r\n" + 
    		"join gescopls.pagos p on ist.socios_id_usuario = p.id_usuario\r\n" + 
    		"join gescopls.estatus_pagos ep on p.id_estatus_pago = ep.id_estatus_pago\r\n" + 
    		"join gescopls.tipos_pagos tp on p.id_tipo_pago = tp.id_tipo_pago\r\n" + 
    		"where ist.Inmueble_id_inmueble = ?1\r\n" + 
    		"and ep.name = ?2\r\n" + 
    		"and tp.name = ?3", 
			nativeQuery = true)
	Long countByInmuebleIdAndEstatusPagoNameAndTipoPagoName(Long inmuebleId, String estatusPagoName, String tipoPagoName);
    
    @Query(value = "SELECT count(p.id_pago) FROM gescopls.inmuebles_socios ist\r\n" + 
    		"join gescopls.pagos p on ist.socios_id_usuario = p.id_usuario\r\n" + 
    		"join gescopls.tipos_pagos tp on p.id_tipo_pago = tp.id_tipo_pago\r\n" + 
    		"where ist.Inmueble_id_inmueble = ?1\r\n" + 
    		"and tp.name = ?2", 
			nativeQuery = true)
	Long countByInmuebleIdAndTipoPagoName(Long id, String tipoPagoName);
    
    @Query(value = "SELECT count(p.id_pago) FROM gescopls.inmuebles_socios ist\n" + 
    		"join gescopls.pagos p on ist.socios_id_usuario = p.id_usuario\n" + 
    		"where ist.Inmueble_id_inmueble = ?1", 
			nativeQuery = true)
	Long countByInmuebleId(Long id);
    
    @Query(value = "select p.* from  gescopls.pagos p\r\n" + 
    		"inner join  gescopls.usuarios s on p.id_usuario = s.id_usuario\r\n" + 
    		"where s.id_usuario in (SELECT ims.socios_id_usuario FROM gescopls.zonas z\r\n" + 
    		"inner join gescopls.asentamientos a on z.codigo = a.id_zona_fk\r\n" + 
    		"inner join gescopls.direcciones d on a.id_asentamiento = d.id_asentamiento_fk\r\n" + 
    		"inner join gescopls.inmuebles i on d.id_direccion = i.id_direccion_fk \r\n" + 
    		"inner join gescopls.inmuebles_socios ims on i.id_inmueble = ims.Inmueble_id_inmueble\r\n" + 
    		"where z.codigo = ?1)", 
			nativeQuery = true)
	Collection<Pago> findBycodigoZona(String codigoZona);
    
    @Query(value = "select p.* from gescopls.usuarios u \r\n" + 
    		"inner join gescopls.inmuebles i on u.id_usuario = i.id_contador_fk \r\n" + 
    		"inner join gescopls.inmuebles_socios ist on i.id_inmueble = ist.Inmueble_id_inmueble \r\n" + 
    		"join gescopls.pagos p on ist.socios_id_usuario = p.id_usuario \r\n" + 
    		"where u.id_usuario = ?1 order by p.id_pago",
			nativeQuery = true)
	Collection<Pago> findByContadorId(Long id);
    
    @Query(value = "select p.* from gescopls.usuarios u \r\n" + 
    		"inner join gescopls.inmuebles i on u.id_usuario = i.id_admin_bi_fk \r\n" + 
    		"inner join gescopls.inmuebles_socios ist on i.id_inmueble = ist.Inmueble_id_inmueble \r\n" + 
    		"join gescopls.pagos p on ist.socios_id_usuario = p.id_usuario \r\n" + 
    		"where u.id_usuario = ?1 order by p.id_pago",
			nativeQuery = true)
	Collection<Pago> findByAdminBiId(Long id);
    
    @Query(value = "select p.* from gescopls.usuarios az \r\n" + 
    		"inner join gescopls.zonas z on az.id_usuario = z.id_admin_zona_fk\r\n" + 
    		"inner join gescopls.asentamientos a on  z.codigo = a.id_zona_fk\r\n" + 
    		"inner join gescopls.direcciones d on a.id_asentamiento = d.id_asentamiento_fk\r\n" + 
    		"inner join gescopls.inmuebles i on d.id_direccion = i.id_direccion_fk\r\n" + 
    		"inner join gescopls.inmuebles_socios ist on i.id_inmueble = ist.Inmueble_id_inmueble\r\n" + 
    		"join gescopls.pagos p on ist.socios_id_usuario = p.id_usuario\r\n" + 
    		"where az.id_usuario = ?1 order by p.id_pago",
			nativeQuery = true)
	Collection<Pago> findByAdminZonaId(Long id);

}
