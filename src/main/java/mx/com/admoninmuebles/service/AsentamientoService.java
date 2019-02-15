package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ColoniaDto;

public interface AsentamientoService {
	
	Collection<ColoniaDto> findBycodigoPostalAndZonaCodigo(final String codigoPostal, String zonaCodigo);

}
