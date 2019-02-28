package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.AreaComun;

@Repository
public interface AreaComunRepository extends CrudRepository<AreaComun, Long> {
	
	Collection<AreaComun> findByInmuebleId(Long id);
	Collection<AreaComun> findByInmuebleAdminBiId(Long id);
	
    @Query(value = "select ac.* from gescopls.usuarios az \r\n" + 
    		"inner join gescopls.zonas z on az.id_usuario = z.id_admin_zona_fk\r\n" + 
    		"inner join gescopls.asentamientos a on  z.codigo = a.id_zona_fk\r\n" + 
    		"inner join gescopls.direcciones d on a.id_asentamiento = d.id_asentamiento_fk\r\n" + 
    		"inner join gescopls.inmuebles i on d.id_direccion = i.id_direccion_fk\r\n" + 
    		"inner join gescopls.areas_comunes ac on i.id_inmueble = ac.id_inmueble_fk\r\n" + 
    		"where az.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
    Collection<AreaComun> findByAdminZonaId(Long id);


}
