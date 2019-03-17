package mx.com.admoninmuebles.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.TipoTicketDto;
import mx.com.admoninmuebles.dto.ZonaDto;
import mx.com.admoninmuebles.persistence.model.TipoTicket;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.GraficoDonaTicketService;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.TipoTicketService;
import mx.com.admoninmuebles.service.ZonaService;

@Controller
public class GraficoDonaTickeController {
	
	Logger logger = LoggerFactory.getLogger(GraficoDonaTickeController.class);

	@Autowired
	private TipoTicketService tipoTicketService;

	@Autowired
	private InmuebleService inmuebleService;
	
	@Autowired
	private ZonaService zonaService;
	
//	@PreAuthorize("hasAuthority('" + PrivilegioConst.VER_TICKET + "')")
//	@GetMapping(value = "/tickets/filtro")
//	public String filtarTickets(final Model model, final HttpServletRequest request, 
//			@RequestParam(name = "inmuebleId", required = false) Long inmuebleId,
//			@RequestParam(name = "tipoTicketId", required = false) Long tipoTicketId, 
//			@RequestParam(name = "estatusTicketName", required = false) String estatusTicketName) {
//		
//		logger.info("inmuebleId " + inmuebleId);
//		logger.info("tipoTicketId " + tipoTicketId);
//		logger.info("estatusTicketName " + estatusTicketName);
//		
////		Optional<Long> optId = SecurityUtils.getCurrentUserId();
////		String nombreTicketsSesion = "tickets";
////		
////		if (request.isUserInRole(RolConst.ROLE_PROVEEDOR)) {
////			model.addAttribute(nombreTicketsSesion, graficoDonaTicketService.findByUsuarioAsignadoIdAndEstatus( optId.get() , estatusTicketName) );
////		} else {
////			model.addAttribute(nombreTicketsSesion, graficoDonaTicketService.filtrarTickets(inmuebleId, tipoTicketId, estatusTicketName));
////		}
//		
//		model.addAttribute("tickets" , graficoDonaTicketService.filtrarTickets(inmuebleId, tipoTicketId, estatusTicketName));
//		return "ticket/tickets";
//	}
//	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR', 'PROVEEDOR')")
	@GetMapping(value = "/tickets/grafica/dona")
	public String mostrarGrafica(Model model, final HttpServletRequest request, final HttpSession session) {

		Collection<TipoTicketDto> tiposTicket = new ArrayList<TipoTicketDto>();
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		
		
		if (request.isUserInRole(RolConst.ROLE_CONTADOR)) {
			TipoTicketDto tipoTicket = tipoTicketService.findById( TipoTicket.FINANCIERO );
			tiposTicket.add( tipoTicket );
			
			Collection<InmuebleDto> inmuebles = inmuebleService.findByContadorId(usuarioLogueadoId);
			if( inmuebles != null && !inmuebles.isEmpty() ) {
				session.setAttribute("inmuebleSeleccionado", inmuebles.stream().findFirst().get().getId() );
			}
			model.addAttribute("inmuebles", inmuebles);
		} else if (request.isUserInRole(RolConst.ROLE_PROVEEDOR)) {
			TipoTicketDto tipoTicket = tipoTicketService.findById( TipoTicket.OPERACION );
			tiposTicket.add( tipoTicket );
			
			Collection<InmuebleDto> inmuebles = inmuebleService.findInmueblesByTicketAsignedToProveedorId( usuarioLogueadoId );
			if( inmuebles != null && !inmuebles.isEmpty() ) {
				session.setAttribute("inmuebleSeleccionado", inmuebles.stream().findFirst().get().getId() );
			}
			model.addAttribute("inmuebles", inmuebles);
		} else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
			tiposTicket = tipoTicketService.findAll();
			Collection<InmuebleDto> inmuebles = inmuebleService.findByAdminBiId(usuarioLogueadoId);
			if( inmuebles != null && !inmuebles.isEmpty() ) {
				session.setAttribute("inmuebleSeleccionado", inmuebles.stream().findFirst().get().getId() );
			}
			model.addAttribute("inmuebles", inmuebles);
		} else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
			tiposTicket = tipoTicketService.findAll();
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
			tiposTicket = tipoTicketService.findAll();
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
		} 

		session.setAttribute("tipoTicketSeleccionado", tiposTicket.stream().findFirst().get().getId() );
		model.addAttribute( "tiposTicket", tiposTicket );

		return "ticket/ticket-grafica";
	}

}
