package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ZonaDto;
import mx.com.admoninmuebles.persistence.model.Zona;

public interface ZonaService {

	Zona save(ZonaDto zonaDto);

	Collection<ZonaDto> findAll();

	ZonaDto findById(String codigo);

	Collection<ZonaDto> findByAdminZonaId(Long id);
	
	Collection<ZonaDto> findByAdministradoresBiId(Long id);

	void deleteById(String codigo);

	boolean exist(String codigo);
	
//	ZonaDto findByAdministradorBiId(final Long id);
}
