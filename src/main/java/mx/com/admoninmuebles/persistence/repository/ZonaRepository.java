package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.persistence.model.Zona;

@Repository
public interface ZonaRepository extends CrudRepository<Zona, String> {
	
	Collection<Zona> findByAdminZonaId(Long id);
	
	Collection<Zona> findByAdministradoresBiId(Long id);
	
    @Query(value = "select z.* from gescopls.zonas z\r\n" + 
    		"join gescopls.zonas_administradoresBi za on z.codigo = za.zona_codigo\r\n" + 
    		"where za.administradoresbi_id_usuario = ?1", 
			nativeQuery = true)
    Optional<Zona> findByAdministradorBiId(Long id);
	
    @Query(value = "select z.* from gescopls.zonas z\r\n" + 
    		"	join gescopls.zonas_proveedores zp on z.codigo = zp.zona_codigo\r\n" + 
    		"    where zp.proveedores_id_usuario = ?1", 
			nativeQuery = true)
	Optional<Zona> findByProveedorId(Long id);
    
    @Transactional
    @Modifying 
    @Query(value = "update gescopls.zonas_proveedores set zona_codigo = :codigoZona where proveedores_id_usuario = :userId",
            nativeQuery = true)
    void updateZonaProveedor(@Param("codigoZona") String codigoZona, @Param("userId") Long userId);
    
    @Transactional
    @Modifying 
    @Query(value = "update gescopls.zonas_administradoresBi set zona_codigo = :codigoZona where administradoresbi_id_usuario = :userId",
            nativeQuery = true)
    void updateZonaAdministradoresBi(@Param("codigoZona") String codigoZona, @Param("userId") Long userId);
	
}
