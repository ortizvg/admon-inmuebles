package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ReporteMensualDto;

public interface ReporteMensualService {
	
	ReporteMensualDto guardar( ReporteMensualDto reporteMensualDto );
	ReporteMensualDto actualizar( ReporteMensualDto reporteMensualDto );
	void eliminar( Long idReporte );
	ReporteMensualDto buscarPorId( Long idReporte );
	Collection<ReporteMensualDto> buscarPorInmuebleId( Long idInmueble );
	Collection<ReporteMensualDto> buscarPorContadorId(Long idContador);
	Collection<ReporteMensualDto> buscarReciente5PorSocioId(Long idSocio);
	Collection<ReporteMensualDto> buscarActualPorInmuebleId(Long idInmueble);
	Collection<ReporteMensualDto> buscarActualPorContadorId(Long idContador);
	Collection<ReporteMensualDto> buscarPorAdminBiId(Long idAdminBi);
	Collection<ReporteMensualDto> buscarPorAdminZonaId(Long idAdminZona);
	Collection<ReporteMensualDto> buscarTodo();

}
