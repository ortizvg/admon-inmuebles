package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ReporteMensualDto;

public interface ReporteMensualService {
	
	ReporteMensualDto guardar( ReporteMensualDto reporteMensualDto );
	ReporteMensualDto actualizar( ReporteMensualDto reporteMensualDto );
	void eliminar( Long idReporte );
	ReporteMensualDto buscarPorId( Long idReporte );
	Collection<ReporteMensualDto> buscarPorInmuebleId( Long idInmueble );

}
