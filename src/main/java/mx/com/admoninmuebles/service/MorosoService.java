package mx.com.admoninmuebles.service;

import java.util.Locale;

import mx.com.admoninmuebles.dto.GraficaDonaMorrisDto;
import mx.com.admoninmuebles.dto.ReporteInmuebleMorososDto;

public interface MorosoService {
	
	ReporteInmuebleMorososDto generarReporteMorososPorInmuebleId( final Long inmuebleId );
	
	GraficaDonaMorrisDto generarGraficaDonaPorInmuebleId( final Long inmuebleId, Locale locale );
	
	void enviarNotificacionRecordatorioPago( Long usuarioId, Locale locale );
	
	

}
