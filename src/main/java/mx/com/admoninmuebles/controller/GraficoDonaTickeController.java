package mx.com.admoninmuebles.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mx.com.admoninmuebles.constant.PrivilegioConst;
import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.TicketDto;
import mx.com.admoninmuebles.dto.ZonaDto;
import mx.com.admoninmuebles.persistence.model.Ticket;
import mx.com.admoninmuebles.persistence.repository.TicketRepository;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.TipoTicketService;
import mx.com.admoninmuebles.service.ZonaService;

@Controller
public class GraficoDonaTickeController {

	@Autowired
	private TipoTicketService tipoTicketService;

	@Autowired
	private InmuebleService inmuebleService;
	
	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private ZonaService zonaService;
	
    @Autowired
    private ModelMapper modelMapper;
	
	@PreAuthorize("hasAuthority('" + PrivilegioConst.VER_TICKET + "')")
	@GetMapping(value = "/tickets/filtro")
	public String filtarTickets(final Model model, final HttpServletRequest request, 
			@RequestParam(name = "inmuebleId", required = false) Long inmuebleId,
			@RequestParam(name = "tipoTicketName", required = false) String tipoTicketName, 
			@RequestParam(name = "estatusTicketName", required = false) String estatusTicketName) {
		
		Optional<Long> optId = SecurityUtils.getCurrentUserId();
		String nombreTicketsSesion = "tickets";
		if (request.isUserInRole(RolConst.ROLE_PROVEEDOR)) {
//			Collection<Ticket> tickets = ticketRepository.findByUsuarioAsignadoIdAndEstatusTicketName(optId.get() , estatusTicketName);
//			Collection<TicketDto> ticketsDto =  tickets.stream().map(ticket -> modelMapper.map(ticket, TicketDto.class)).collect(Collectors.toList());
			Collection<TicketDto> ticketsDto = StreamSupport.stream(ticketRepository.findAll().spliterator(), false).map(ticket -> modelMapper.map(ticket, TicketDto.class)).collect(Collectors.toList());;
			model.addAttribute(nombreTicketsSesion, ticketsDto);
		} else {
			Collection<Ticket> tickets = ticketRepository.findByInmuebleIdAndTipoTicketNameAndEstatusTicketName(inmuebleId, tipoTicketName, estatusTicketName);
			Collection<TicketDto> ticketsDto =  tickets.stream().map(ticket -> modelMapper.map(ticket, TicketDto.class)).collect(Collectors.toList());
			model.addAttribute(nombreTicketsSesion, ticketsDto);
		}
		return "ticket/tickets";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR', 'PROVEEDOR')")
	@GetMapping(value = "/tickets/grafica/dona")
	public String mostrarGrafica(Model model, final HttpServletRequest request, final HttpSession session) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();

		if (request.isUserInRole(RolConst.ROLE_CONTADOR)) {
			Collection<InmuebleDto> inmuebles = inmuebleService.findByContadorId(usuarioLogueadoId);
			if( inmuebles != null && !inmuebles.isEmpty() ) {
				session.setAttribute("inmuebleSeleccionado", inmuebles.stream().findFirst().get().getId() );
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


		session.setAttribute( "tiposTicket", tipoTicketService.findAll() );

		return "tickets/ticket-grafica";
	}

}
