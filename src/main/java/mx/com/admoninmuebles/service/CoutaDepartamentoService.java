package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.CuotaDepartamentoDto;

public interface CoutaDepartamentoService {
	
	CuotaDepartamentoDto guardar( CuotaDepartamentoDto cuotaDepartamentoDto );
	CuotaDepartamentoDto actualizar( CuotaDepartamentoDto cuotaDepartamentoDto );
	void eliminar( Long idCuotadepartamento );
	CuotaDepartamentoDto buscarPorId( Long idCuotadepartamento );
	Collection<CuotaDepartamentoDto> buscarPorInmuebleId( Long idInmueble );
	Collection<CuotaDepartamentoDto> buscarPorSocioId( Long idsocio );
	CuotaDepartamentoDto buscarRecientePorSocioId( Long idsocio );

}
