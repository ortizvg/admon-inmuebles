package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.CuotaDepartamentoDto;

public interface CuotaDepartamentoService {
	
	CuotaDepartamentoDto guardar( CuotaDepartamentoDto cuotaDepartamentoDto );
	CuotaDepartamentoDto actualizar( CuotaDepartamentoDto cuotaDepartamentoDto );
	void eliminar( Long idCuotaDepartamento );
	CuotaDepartamentoDto buscarPorId( Long idCuotaDepartamento );
	Collection<CuotaDepartamentoDto> buscarPorInmuebleId( Long idInmueble );
	Collection<CuotaDepartamentoDto> buscarPorContadorId(Long idContador);
	Collection<CuotaDepartamentoDto> buscarPorAdminBiId( Long idAdminBi );
	Collection<CuotaDepartamentoDto> buscarPorAdminZonaId( Long idAdminZona );
	Collection<CuotaDepartamentoDto> buscarTodo();

}
