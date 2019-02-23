package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import mx.com.admoninmuebles.persistence.model.Reglamento;

public interface ReglamentoRepository  extends CrudRepository<Reglamento, Long> {
	
	Collection<Reglamento> findByInmuebleId( Long inmuebleId );
	
    @Query(value = "select r.* from gescopls.usuarios c \r\n" + 
    		"inner join gescopls.inmuebles i on c.id_usuario = i.id_contador_fk\r\n" + 
    		"inner join gescopls.reglamentos r on i.id_inmueble = r.id_inmueble_fk\r\n" + 
    		" where c.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<Reglamento> findByContadorId( Long contadorId );
    
    @Query(value = "select r.* from gescopls.usuarios c \r\n" + 
    		"inner join gescopls.inmuebles i on c.id_usuario = i.id_admin_bi_fk\r\n" + 
    		"inner join gescopls.reglamentos r on i.id_inmueble = r.id_inmueble_fk\r\n" + 
    		" where c.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<Reglamento> findByAdminBiId( Long adminBiId );
    
    @Query(value = "select r.* from gescopls.usuarios az \r\n" + 
    		"inner join gescopls.zonas z on az.id_usuario = z.id_admin_zona_fk\r\n" + 
    		"inner join gescopls.asentamientos a on  z.codigo = a.id_zona_fk\r\n" + 
    		"inner join gescopls.direcciones d on a.id_asentamiento = d.id_asentamiento_fk\r\n" + 
    		"inner join gescopls.inmuebles i on d.id_direccion = i.id_direccion_fk\r\n" + 
    		"inner join gescopls.reglamentos r on i.id_inmueble = r.id_inmueble_fk\r\n" + 
    		"where az.id_usuario = ?1 order by i.id_inmueble desc",
			nativeQuery = true)
	Collection<Reglamento> findByAdminZonaId( Long adminZonaId );

}
