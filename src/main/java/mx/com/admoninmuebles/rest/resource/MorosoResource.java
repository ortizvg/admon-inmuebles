package mx.com.admoninmuebles.rest.resource;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.admoninmuebles.dto.GraficaDonaMorrisDto;
import mx.com.admoninmuebles.dto.ReporteInmuebleMorososDto;
import mx.com.admoninmuebles.dto.RestResponseDto;
import mx.com.admoninmuebles.service.MorosoService;

@RestController
@RequestMapping("/api")
public class MorosoResource {
	
	@Autowired
	private MorosoService morosoService;
	
    @Autowired
    private MessageSource messages;
	
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR')")
	@GetMapping("/morosos/inmuebles/{id}/reporte")
    public ResponseEntity<ReporteInmuebleMorososDto> buscarSociosPorInmueble( @PathVariable Long id ) {
        try {
        	ReporteInmuebleMorososDto reporteMorososPorInmueble =  morosoService.generarReporteMorososPorInmuebleId( id );
            return new ResponseEntity<>(reporteMorososPorInmueble, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ReporteInmuebleMorososDto(), HttpStatus.OK);
        }
    }
	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR')")
	@GetMapping("/morosos/inmuebles/{id}/grafica/dona")
    public ResponseEntity<GraficaDonaMorrisDto> generarGraficaDonaPorInmueble( @PathVariable Long id, Locale locale) {
        try {
        	 GraficaDonaMorrisDto graficaPagosPorInmueble =  morosoService.generarGraficaDonaPorInmuebleId( id, locale );
            return new ResponseEntity<>(graficaPagosPorInmueble, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR')")
	@GetMapping(value = "/morosos/pagos/{id}/notificacion")
	public ResponseEntity<RestResponseDto> enviarNotificacion(@PathVariable Long id, Locale locale ) {
		
		RestResponseDto respuesta = new RestResponseDto();
		respuesta.setCodigo(HttpStatus.OK.toString());
		 try {
        	 morosoService.enviarNotificacionRecordatorioPago( id, locale );
        	 respuesta.setDescripcion( messages.getMessage("morosos.notificacion.recordatorio.pago.exito", null, locale) );
             return new ResponseEntity<RestResponseDto>(respuesta, HttpStatus.OK);
        } catch (Exception e) {
        	 respuesta.setDescripcion( messages.getMessage("morosos.notificacion.recordatorio.pago.error", null, locale) );
            return new ResponseEntity<RestResponseDto>(respuesta, HttpStatus.OK);
        }
	}

}
