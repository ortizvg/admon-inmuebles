package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ReglamentoDto;

public interface ReglamentoService {
	
	ReglamentoDto guardar( ReglamentoDto reglamentoDto );
	ReglamentoDto actualizar( ReglamentoDto reglamentoDto );
	void eliminar( Long idReglamento );
	ReglamentoDto buscarPorId( Long idReglamento );
	Collection<ReglamentoDto> buscarPorInmuebleId( Long idInmueble );

}
