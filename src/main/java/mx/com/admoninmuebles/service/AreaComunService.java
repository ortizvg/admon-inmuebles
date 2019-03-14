package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.AreaComunDto;
import mx.com.admoninmuebles.persistence.model.AreaComun;

public interface AreaComunService {
	AreaComun save(AreaComunDto areaComunDto);

	Collection<AreaComunDto> findAll();
	
	Collection<AreaComunDto> findByInmuebleId(Long idInmueble);

	AreaComunDto findById(Long id);

	void delete(Long id);
	
	Collection<AreaComunDto> findByAdminBiId(Long id);
	
	Collection<AreaComunDto> findByAdminZonaId(Long id);
	
    boolean isFiltro( AreaComunDto areaComunDto  );
    
    Collection<AreaComunDto> filtrar( AreaComunDto areaComunDto );
}
