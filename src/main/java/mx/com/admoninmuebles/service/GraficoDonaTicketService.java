package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.GraficaDonaMorrisDto;
import mx.com.admoninmuebles.dto.ReporteTicketDto;
import mx.com.admoninmuebles.dto.TicketDto;

public interface GraficoDonaTicketService {
	
	ReporteTicketDto generarReporteTicketsPorInmuebleId(final Long inmuebleId,  Long tipoTicketId) ;
	
	GraficaDonaMorrisDto generarGraficaDona( Long inmuebleId,  Long tipoTicketId );
	
	Collection<TicketDto> findByUsuarioAsignadoIdAndEstatus(Long usuarioAsignadoId, String estatus);
	
	Collection<TicketDto> filtrarTickets(Long inmuebleId, Long tipoTicketId, String estatus);
	
}
