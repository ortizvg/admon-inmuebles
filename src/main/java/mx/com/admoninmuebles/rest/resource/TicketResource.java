package mx.com.admoninmuebles.rest.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.admoninmuebles.dto.GraficaDonaMorrisDto;
import mx.com.admoninmuebles.dto.ReporteTicketDto;
import mx.com.admoninmuebles.service.GraficoDonaTicketService;

@RestController
@RequestMapping("/api")
public class TicketResource {
	
	@Autowired
	private GraficoDonaTicketService graficoDonaTicketService;
	
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR')")
	@GetMapping("/tickets/reporte")
    public ResponseEntity<ReporteTicketDto> buscarSociosPorInmueble( @RequestParam(name = "inmuebleId", required = false) Long inmuebleId,
			@RequestParam(name = "tipoTicketId", required = false) String tipoTicketId  ) {
        try {
        	ReporteTicketDto reporteTicketDto =  graficoDonaTicketService.generarReporteTicketsPorInmuebleId( inmuebleId, tipoTicketId);
            return new ResponseEntity<>(reporteTicketDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ReporteTicketDto(), HttpStatus.OK);
        }
    }
	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR')")
	@GetMapping("/tickets/grafica/dona")
    public ResponseEntity<GraficaDonaMorrisDto> generarGraficaDonaTickets(
			@RequestParam(name = "inmuebleId", required = false) Long inmuebleId,
			@RequestParam(name = "tipoTicketId", required = false) String tipoTicketId ) {
		
        try {
        	
        	GraficaDonaMorrisDto graficaPagosPorInmueble =  graficoDonaTicketService.generarGraficaDona( inmuebleId, tipoTicketId);
        	 
            return new ResponseEntity<>(graficaPagosPorInmueble, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
