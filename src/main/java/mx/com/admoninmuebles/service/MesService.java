package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.MesDto;

public interface MesService {
	
	Collection<MesDto> buscarTodos();
	MesDto buscarPorId( Long id );

}
