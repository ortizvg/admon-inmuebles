package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.TipoSocioDto;

public interface TipoSocioService {
	
    Collection< TipoSocioDto> findAll();
    Collection< TipoSocioDto> findAllByLang(String lang);
    TipoSocioDto findById(Long idTipoSocio);

}
