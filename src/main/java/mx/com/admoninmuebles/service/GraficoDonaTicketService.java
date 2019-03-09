package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.GraficaDonaMorrisDto;
import mx.com.admoninmuebles.dto.ReporteTicketDto;

public interface GraficoDonaTicketService {
	
	ReporteTicketDto generarReporteTicketsPorInmuebleId(final Long inmuebleId,  String tipoTicketId) ;
	
	GraficaDonaMorrisDto generarGraficaDona( Long inmuebleId,  String tipoTicketId );

}
