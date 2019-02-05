package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ArchivoDto;

public interface ArchivoService {
	
	ArchivoDto guardar(ArchivoDto archivoDto);
	ArchivoDto actualizar(ArchivoDto archivoDto);
	void eliminar(ArchivoDto archivoDto);
	ArchivoDto buscarPorId(String id);
	Collection<ArchivoDto> buscarTodo(); 

}
