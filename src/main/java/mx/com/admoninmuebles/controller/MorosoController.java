package mx.com.admoninmuebles.controller;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.ZonaDto;
import mx.com.admoninmuebles.persistence.model.TipoPago;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.NotificacionService;
import mx.com.admoninmuebles.service.PagoService;
import mx.com.admoninmuebles.service.ZonaService;

@Controller
public class MorosoController {

	@Autowired
	private ZonaService zonaService;

	@Autowired
	private InmuebleService inmuebleService;
	
	@Autowired
	private NotificacionService notificacionService;
	
	@Autowired
	private PagoService pagoService;
	

	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR')")
	@GetMapping(value = "/morosos/tablero")
	public String mostrarTablero(Model model, final HttpServletRequest request,final HttpSession session) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();

		if (request.isUserInRole(RolConst.ROLE_CONTADOR)) {
			Collection<InmuebleDto> inmuebles = inmuebleService.findByContadorId(usuarioLogueadoId);
			if( inmuebles != null && !inmuebles.isEmpty() ) {
				session.setAttribute("inmuebleSeleccionado", inmuebles.stream().findFirst().get().getId() );
				session.setAttribute("notificaciones", notificacionService.findByUserIdNotExpired( usuarioLogueadoId ));
			}
			model.addAttribute("inmuebles", inmuebles);
		} else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
			Collection<InmuebleDto> inmuebles = inmuebleService.findByAdminBiId(usuarioLogueadoId);
			if( inmuebles != null && !inmuebles.isEmpty() ) {
				session.setAttribute("inmuebleSeleccionado", inmuebles.stream().findFirst().get().getId() );
			}
			model.addAttribute("inmuebles", inmuebles);
		} else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
			
			Collection<ZonaDto> zonas = zonaService.findByAdminZonaId( usuarioLogueadoId );
			
			if( zonas != null && !zonas.isEmpty() ) {
				String zonaCodigoSeleccionada = zonas.stream().findFirst().get().getCodigo();
				Collection<InmuebleDto> inmuebles = inmuebleService.findByZonaCodigo( zonaCodigoSeleccionada );
				session.setAttribute("zonaCodigoSeleccionada", zonaCodigoSeleccionada );
				model.addAttribute("inmuebles", inmuebles);
				if( inmuebles != null && !inmuebles.isEmpty() ) {
					session.setAttribute("inmuebleSeleccionado", inmuebles.stream().findFirst().get().getId() );
				}
			}
			
			model.addAttribute("zonas", zonas);
		} else if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
			
			Collection<ZonaDto> zonas = zonaService.findAll();
			
			if( zonas != null && !zonas.isEmpty() ) {
				String zonaCodigoSeleccionada = zonas.stream().findFirst().get().getCodigo();
				Collection<InmuebleDto> inmuebles = inmuebleService.findByZonaCodigo( zonaCodigoSeleccionada );
				session.setAttribute("zonaCodigoSeleccionada", zonaCodigoSeleccionada );
				model.addAttribute("inmuebles", inmuebles);
				if( inmuebles != null && !inmuebles.isEmpty() ) {
					session.setAttribute("inmuebleSeleccionado", inmuebles.stream().findFirst().get().getId() );
				}
			}
			
			model.addAttribute("zonas", zonas);
		} else {
			model.addAttribute("pagos", Collections.emptyList());
		}

		return "morosos/morosos-tablero";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR')")
	@GetMapping(value = "/morosos/inmuebles/{idInmueble}/detalle/{nombreStatusPago}")
	public String mostrarDetalleMorosos(Model model, final HttpServletRequest request, @PathVariable final Long idInmueble, @PathVariable final String nombreStatusPago, final HttpSession session ) {
		
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		
		if( !inmuebleService.exist(idInmueble) )  {
			return "error/404";
		}
		
		Collection<InmuebleDto> inmuebles = null;
		if (request.isUserInRole(RolConst.ROLE_CONTADOR)) {
			inmuebles = inmuebleService.findByContadorId(usuarioLogueadoId);
		} else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
			inmuebles = inmuebleService.findByAdminBiId(usuarioLogueadoId);
		} else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
			inmuebles = inmuebleService.findByAdminZonaId(usuarioLogueadoId);
		} 
		
		if( !request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
			if( inmuebles == null || inmuebles.isEmpty() ) {
				return "error/404";
			} else {
				InmuebleDto inmuebleDto = inmuebleService.findById( idInmueble );
				
				if(!inmuebles.stream().anyMatch( inmueble -> inmueble.getId() == inmuebleDto.getId()) ) {
					return "error/404";
				}
			}
		}
		
		session.setAttribute("idInmueble", idInmueble );
		session.setAttribute("nombreStatusPago", nombreStatusPago );
		model.addAttribute("pagos", pagoService.buscarPorInmuebleYEstatusPagoNombreYTipoPagoNombre( idInmueble, nombreStatusPago, TipoPago.CUOTA ) );

		return "morosos/morosos-detalle";
	}
	
}
