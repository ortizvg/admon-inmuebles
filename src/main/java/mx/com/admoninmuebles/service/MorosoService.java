package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.GraficaDonaMorrisDto;
import mx.com.admoninmuebles.dto.ReporteInmuebleMorososDto;

public interface MorosoService {
	
	ReporteInmuebleMorososDto generarReporteMorososPorInmuebleId( final Long inmuebleId );
	
	GraficaDonaMorrisDto generarGraficaDonaPorInmuebleId( final Long inmuebleId );
	
	void enviarNotificacionRecordatorioPago( Long usuarioId );
	
	

}
