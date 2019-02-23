package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.Comunicado;

public interface ComunicadoRepository extends CrudRepository<Comunicado, Long> {
	
	Collection<Comunicado> findByInmuebleId( Long inmuebleId );
	
    @Query(value = "select co.* from gescopls.usuarios c \r\n" + 
    		"inner join gescopls.inmuebles i on c.id_usuario = i.id_contador_fk\r\n" + 
    		"inner join gescopls.comunicados co on i.id_inmueble = co.id_inmueble_fk\r\n" + 
    		" where c.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<Comunicado> findByContadorId( Long contadorId );
    
    @Query(value = "select co.* from gescopls.usuarios c \r\n" + 
    		"inner join gescopls.inmuebles i on c.id_usuario = i.id_admin_bi_fk\r\n" + 
    		"inner join gescopls.comunicados co on i.id_inmueble = co.id_inmueble_fk\r\n" + 
    		"where c.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<Comunicado> findByAdminBiId( Long adminBiId );
    
    @Query(value = "select c.* from gescopls.usuarios az \r\n" + 
    		"inner join gescopls.zonas z on az.id_usuario = z.id_admin_zona_fk\r\n" + 
    		"inner join gescopls.asentamientos a on  z.codigo = a.id_zona_fk\r\n" + 
    		"inner join gescopls.direcciones d on a.id_asentamiento = d.id_asentamiento_fk\r\n" + 
    		"inner join gescopls.inmuebles i on d.id_direccion = i.id_direccion_fk\r\n" + 
    		"inner join gescopls.comunicados c on i.id_inmueble = c.id_inmueble_fk\r\n" + 
    		"where az.id_usuario =  ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<Comunicado> findByAdminZonaId( Long adminZonaId );

}
