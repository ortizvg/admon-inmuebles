package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.EstadoCorreoDto;

public interface EstadoCorreoService {
	
	Collection<EstadoCorreoDto> findAll();
	
	EstadoCorreoDto findById(Long id);

}
