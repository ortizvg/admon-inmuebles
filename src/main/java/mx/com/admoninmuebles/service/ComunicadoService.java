package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ComunicadoDto;
import mx.com.admoninmuebles.dto.ReglamentoDto;

public interface ComunicadoService {
	
	ComunicadoDto guardar( ComunicadoDto comunicadoDto );
	ComunicadoDto actualizar( ComunicadoDto comunicadoDto );
	void eliminar( Long idComunicado );
	ComunicadoDto buscarPorId( Long idComunicado );
	Collection<ComunicadoDto> buscarPorInmuebleId( Long idInmueble );

}
