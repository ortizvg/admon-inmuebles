package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.AreaComunDto;
import mx.com.admoninmuebles.persistence.model.AreaComun;

public interface AreaComunService {
	AreaComun save(AreaComunDto areaComunDto);

	Collection<AreaComunDto> findAll();

	AreaComunDto findById(Long id);

	void delete(Long id);
}
