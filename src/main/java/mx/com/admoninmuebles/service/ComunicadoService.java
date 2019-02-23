package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ComunicadoDto;

public interface ComunicadoService {
	
	ComunicadoDto guardar( ComunicadoDto comunicadoDto );
	ComunicadoDto actualizar( ComunicadoDto comunicadoDto );
	void eliminar( Long idComunicado );
	ComunicadoDto buscarPorId( Long idComunicado );
	Collection<ComunicadoDto> buscarTodo();
	Collection<ComunicadoDto> buscarPorInmuebleId( Long idInmueble );
	Collection<ComunicadoDto> buscarPorContadorId(Long idContador);
	Collection<ComunicadoDto> buscarPorAdminBiId(Long idAdminBi);
	Collection<ComunicadoDto> buscarPorAdminZonaId(Long idAdminZona);

}
