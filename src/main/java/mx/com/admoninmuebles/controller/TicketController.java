package mx.com.admoninmuebles.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.com.admoninmuebles.constant.ComunConst;
import mx.com.admoninmuebles.constant.EstatusTicketConst;
import mx.com.admoninmuebles.constant.PrivilegioConst;
import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.CambioTicketDto;
import mx.com.admoninmuebles.dto.TicketDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.AreaServicioService;
import mx.com.admoninmuebles.service.CambioTicketService;
import mx.com.admoninmuebles.service.NotificacionTicketService;
import mx.com.admoninmuebles.service.TicketService;
import mx.com.admoninmuebles.service.TipoTicketService;
import mx.com.admoninmuebles.service.UsuarioService;
import mx.com.admoninmuebles.util.UtilDate;

@Controller
public class TicketController {

	Logger logger = LoggerFactory.getLogger(TicketController.class);
	
	@Autowired
	private TicketService ticketService;

	@Autowired
	private AreaServicioService areaServicioService;

	@Autowired
	private TipoTicketService tipoTicketService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private NotificacionTicketService notificacionTicketService;
	
	@Autowired
	private ServletContext servletContext;

	@Autowired
	private CambioTicketService cambioTicketService;
	
    @Autowired
    private MessageSource messages;
	
	@PreAuthorize("hasAuthority('" + PrivilegioConst.VER_TICKET + "')")
	@GetMapping(value = "/tickets")
	public String init(final TicketDto ticketDto, final Model model, final HttpServletRequest request) {
		logger.info("TICKETDTO: " + ticketDto.toString() );
		Optional<Long> optId = SecurityUtils.getCurrentUserId();
		String nombreTicketsSesion = "tickets";
		
		if( ticketService.isFiltro( ticketDto ) ) {
 			model.addAttribute(nombreTicketsSesion, ticketService.filtrar( ticketDto ) );
 			return "ticket/tickets";
	 	}
		
		if (request.isUserInRole(RolConst.ROLE_SOCIO_BI)) {
			model.addAttribute(nombreTicketsSesion, revisaRetraso(ticketService.findByUsuarioCreadorId(optId.get())));
		} else if (request.isUserInRole(RolConst.ROLE_PROVEEDOR) || request.isUserInRole(RolConst.ROLE_CONTADOR)) {
			model.addAttribute(nombreTicketsSesion, revisaRetraso(ticketService.findByUsuarioAsignadoId(optId.get())));
		} else if(request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)){
			model.addAttribute(nombreTicketsSesion, revisaRetraso(ticketService.findByAdminZonaId(optId.get())));
		}else if(request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
			model.addAttribute(nombreTicketsSesion, revisaRetraso(ticketService.findByAdminBiId(optId.get())));
		}else {
			model.addAttribute(nombreTicketsSesion, revisaRetraso(ticketService.findAll()));
		}
		return "ticket/tickets";
	}
 
	private Collection<TicketDto> revisaRetraso(Collection<TicketDto> tickets) {
		for (TicketDto ticketDto : tickets) {
			LocalDate fechaCreacion = UtilDate.dateToLocalDate(ticketDto.getFechaCreacion()); 
			if(!EstatusTicketConst.EN_PROCESO.equals(ticketDto.getEstatus()) && ChronoUnit.DAYS.between(fechaCreacion, LocalDate.now()) > 5) {
				ticketDto.setRetraso(true);
			}
		}
		return tickets;
	}
 
	@PreAuthorize("hasAuthority('ABRIR_TICKET')")
	@GetMapping(value = "/ticket-crear")
	public String ticketCrear(final TicketDto ticketDto, final HttpSession session) {
		session.setAttribute("tipoTicket", tipoTicketService.findAll());
		session.setAttribute("areaServicio", areaServicioService.findAll());
		return "ticket/ticket-crear";
	}

	@PreAuthorize("hasAuthority('ABRIR_TICKET')")
	@PostMapping(value = "/ticket")
	public String crearTicket(final Locale locale, @Valid final TicketDto ticketDto,
			final BindingResult bindingResult, @RequestParam("file") MultipartFile file, RedirectAttributes redirect) {
		String showPageFail = "ticket/ticket-crear"; 
		String showPageFailRedirect = "redirect:/ticket-crear"; 
		Optional<Long> optId = SecurityUtils.getCurrentUserId();
		if (optId.isPresent()) {
			ticketDto.setUsuarioCreadorId(optId.get());
		}
    	final String MIME_TYPE_JPG = "image/jpeg";
    	final String MIME_TYPE_PDF = "application/pdf";
    	final String MIME_TYPE_DOC = "application/msword";
    	final String MIME_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    	final String MIME_TYPE_TXT = "text/plain";
    	String fileType = null;
    	CambioTicketDto cambio = new CambioTicketDto();
    	ArrayList<CambioTicketDto> cambios = null;
        try {
            if (!file.isEmpty()) {
                if( file.getSize() > ComunConst.TAMANIO_5_MB ) {
               	 	redirect.addFlashAttribute("error", messages.getMessage("ticket.crear.archivo.validacion.tamanio", null, locale) );
               	 	return showPageFailRedirect;
                }
            	fileType = file.getContentType();
    			if(!(MIME_TYPE_JPG.contains(fileType) || MIME_TYPE_PDF.contains(fileType)
    					|| MIME_TYPE_DOC.contains(fileType) || MIME_TYPE_DOCX.contains(fileType) || MIME_TYPE_TXT.contains(fileType))) {
    				redirect.addFlashAttribute("error", messages.getMessage("ticket.crear.archivo.validacion.tipo", null, locale));
    				return showPageFailRedirect;
    			}else {
    				cambio.setArchivoEvidencia(IOUtils.toByteArray(file.getInputStream()));
    			}
            }
            cambios = new ArrayList<>();
            cambio.setComentario("Alta de ticket");
//            cambios.add(cambio);
			ticketDto.setFechaCreacion(new Date());
			ticketDto.setEstatus(EstatusTicketConst.ABIERTO);
//			ticketDto.setCambios(cambios);
			ticketDto.setTitulo(file.getOriginalFilename());
			TicketDto ticketCreado =  ticketService.save(ticketDto);
			cambio.setTicketId(ticketCreado.getId());
 			cambioTicketService.save(cambio);
 			notificacionTicketService.notificarCreacionTicket( ticketCreado );
		} catch (Exception e) {
			logger.error("Hubo un problema al crear el ticket");
			return showPageFail;
		}
		return "redirect:tickets";
	}

	@PreAuthorize("hasAuthority('ABRIR_TICKET')")
	@GetMapping(value = "/ticket-editar/{id}")
	public String findById(final @PathVariable long id, final Model model, final HttpSession session) {
		model.addAttribute("ticketDto", ticketService.findById(id));
		session.setAttribute("areasServicio", areaServicioService.findAll());
		return "ticket/ticket-crear";
	}

	@PreAuthorize("hasAuthority('" + PrivilegioConst.VER_TICKET + "')")
	@GetMapping(value = "/ticket-detalle/{id}")
	public String buscarTicketPorId(final @PathVariable long id, final Model model, final HttpServletRequest request,
			final HttpSession session) {
		String vista;
		TicketDto ticketDto = ticketService.findById(id);
		Optional<Long> optId = SecurityUtils.getCurrentUserId();
		model.addAttribute("ticketDto", ticketDto);
		if (request.isUserInRole(RolConst.ROLE_SOCIO_BI)) {
			vista = "ticket/ticket-detalle";
		} else if (request.isUserInRole(RolConst.ROLE_PROVEEDOR) || request.isUserInRole(RolConst.ROLE_CONTADOR)) {
			vista = "ticket/ticket-aceptar";
		} else {
			vista = "ticket/ticket-asignar";
			session.setAttribute("proveedores", usuarioService.findByRolesNombreAndAreasServicioId(RolConst.ROLE_PROVEEDOR, ticketDto.getAreaServicioId()));
			session.setAttribute("contadores", usuarioService.findByRolesNombre(RolConst.ROLE_CONTADOR));
			List<UsuarioDto> listAdminBi = new ArrayList<>();
			session.setAttribute("admins", listAdminBi.add(usuarioService.findById(optId.get())));
		}
		return vista;
	}
	
	@PreAuthorize("hasAuthority('" + PrivilegioConst.VER_TICKET + "')")
	@GetMapping(value = "/ticket-regresar/{id}")
	public String regresarDetalle(final @PathVariable long id, final Model model, final HttpServletRequest request,
			final HttpSession session) {
		String vista;
		TicketDto ticketDto = ticketService.findById(id);
		model.addAttribute("ticketDto", ticketDto);
		if (request.isUserInRole(RolConst.ROLE_SOCIO_BI)) {
			vista = "redirect:/ticket-detalle";
		} else if (request.isUserInRole(RolConst.ROLE_PROVEEDOR)) {
			vista = "ticket-aceptar";
		} else {
			vista = "ticket-asignar";
			session.setAttribute("proveedores", usuarioService.findByRolesNombreAndAreasServicioId(RolConst.ROLE_PROVEEDOR, ticketDto.getAreaServicioId()));
			session.setAttribute("contadores", usuarioService.findByRolesNombre(RolConst.ROLE_CONTADOR));
		}
		return vista;
	}

	@PreAuthorize("hasAuthority('" + PrivilegioConst.ASIGNAR_TICKET + "')")
	@PostMapping(value = "/asignarTicket")
	public String asignar(final Locale locale, @Valid final TicketDto ticketDto, final BindingResult bindingResult) {
		TicketDto ticketDtoTemp = ticketService.findById(ticketDto.getId());
		ticketDtoTemp.setUsuarioAsignadoId(ticketDto.getUsuarioAsignadoId());
		ticketDtoTemp.setEstatus(EstatusTicketConst.EN_PROCESO);
//		ticketService.save(ticketDtoTemp);
		notificacionTicketService.notificarAsignacionTicket( ticketService.save( ticketDtoTemp ) );
		return "redirect:tickets";
	}

	@PreAuthorize("hasAuthority('" + PrivilegioConst.ACEPTAR_TICKET + "')")
	@GetMapping(value = "/ticket-aceptar/{id}")
	public String aceptar(final @PathVariable long id) {
		TicketDto ticketDtoTemp = ticketService.findById(id);
//		ticketDtoTemp.setEstatus(EstatusTicketConst.ACEPTADO);
		ticketService.save(ticketDtoTemp);
		return "redirect:/tickets";
	}
	
	@PreAuthorize("hasAuthority('" + PrivilegioConst.ATENDER_TICKET + "')")
	@GetMapping(value = "/ticket-rechazar/{id}")
	public String rechazar(final @PathVariable long id) {
		TicketDto ticketDtoTemp = ticketService.findById(id);
//		ticketDtoTemp.setEstatus(EstatusTicketConst.RECHAZADO);
		ticketDtoTemp.setUsuarioAsignadoId(null);
		ticketDtoTemp.setUsuarioAsignadoNombre(null);
		ticketDtoTemp.setUsuarioAsignadoApellidoPaterno(null);
		ticketDtoTemp.setUsuarioAsignadoApellidoMaterno(null);
		
		ticketService.save(ticketDtoTemp);
		return "redirect:/tickets";
	}

//	@PreAuthorize("hasAuthority('" + PrivilegioConst.ASIGNAR_TICKET + "')")
	@GetMapping(value = "/ticket-comentarios/{id}")
	public String comentarios(final @PathVariable long id, final Model model, final HttpServletRequest request,
			final HttpSession session) {
		Collection<CambioTicketDto> cambios = cambioTicketService.findByTicketId(id);
		CambioTicketDto cambioTicketDto = new CambioTicketDto();
		model.addAttribute("cambios", cambios);
		model.addAttribute("idTicket", id);
		model.addAttribute("cambioTicketDto", cambioTicketDto);
		return "ticket/ticket-comentarios";
	}

	@PreAuthorize("hasAuthority('" + PrivilegioConst.ASIGNAR_TICKET + "')")
	@GetMapping(value = "/ticket-terminar/{id}")
	public String terminar(final @PathVariable long id) {
		TicketDto ticketDtoTemp = ticketService.findById(id);
		ticketDtoTemp.setEstatus(EstatusTicketConst.CERRADO);
		TicketDto ticketTerminadoDto = ticketService.save(ticketDtoTemp);
		notificacionTicketService.notificarCierreTicket(ticketTerminadoDto);
		return "redirect:/tickets";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
	@GetMapping(value = "/ticket-eliminar/{id}")
	public String eliminarTicket(final @PathVariable long id) {
		ticketService.delete(id);
		return "redirect:ticket/tickets";
	}
	
	
	@GetMapping(value="/ticket-download/{id}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable long id) {
		CambioTicketDto cambio = cambioTicketService.findById(id);
		ByteArrayResource resource = new ByteArrayResource(cambio.getArchivoEvidencia());
		String strMediaType = servletContext.getMimeType(cambio.getTituloArchivoEvidencia());
	    MediaType mediaType = MediaType.parseMediaType(strMediaType);
	    return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+cambio.getTituloArchivoEvidencia())
                // Content-Type
                .contentType(mediaType) //
                // Content-Lengh
//                .contentLength(ticketDto.getArchivoEvidencia().length)
                .contentLength(cambio.getArchivoEvidencia().length)
                .body(resource);
	}
	
	@PostMapping(value = "/ticket-comentarios/{idTicket}")
	public String agregarComentario(final Locale locale, @Valid final CambioTicketDto cambioTicketDto,@PathVariable Long idTicket, final Model model,
			final BindingResult bindingResult, @RequestParam("file") MultipartFile file, RedirectAttributes redirect) {
		String showPageFail = "ticket/ticket-comentarios"; 
		String showPageFailRedirect = "redirect:/ticket-comentarios/" + idTicket; 
    	final String MIME_TYPE_JPG = "image/jpeg";
    	final String MIME_TYPE_PDF = "application/pdf";
    	final String MIME_TYPE_DOC = "application/msword";
    	final String MIME_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    	final String MIME_TYPE_TXT = "text/plain";
    	String fileType = null;
        try {
            if (!file.isEmpty()) {
            	 if( file.getSize() > ComunConst.TAMANIO_5_MB ) {
                	 	redirect.addFlashAttribute("error", messages.getMessage("ticket.crear.archivo.validacion.tamanio", null, locale) );
                	 	return showPageFailRedirect;
                 }
            	fileType = file.getContentType();
    			if(!(MIME_TYPE_JPG.contains(fileType) || MIME_TYPE_PDF.contains(fileType)
    					|| MIME_TYPE_DOC.contains(fileType) || MIME_TYPE_DOCX.contains(fileType) || MIME_TYPE_TXT.contains(fileType))) {
    				redirect.addFlashAttribute("error", messages.getMessage("ticket.crear.archivo.validacion.tipo", null, locale));
    				return showPageFailRedirect;
    			}else {
    				cambioTicketDto.setArchivoEvidencia(IOUtils.toByteArray(file.getInputStream()));
    				cambioTicketDto.setTituloArchivoEvidencia(file.getOriginalFilename());
    			}
            }
            cambioTicketDto.setTicketId(idTicket);
            CambioTicketDto cambioTicketCreadoDto = cambioTicketService.save(cambioTicketDto);
 			notificacionTicketService.notificarComentarioTicket(cambioTicketCreadoDto);
 			Collection<CambioTicketDto> cambios = cambioTicketService.findByTicketId(idTicket);
 			model.addAttribute("cambios", cambios);
 			model.addAttribute("idTicket", idTicket);
		} catch (Exception e) {
			logger.error("Hubo un problema al agregar comentario al ticket: "+idTicket, e);
			return showPageFailRedirect;
		}
		return "ticket/ticket-comentarios";
	}

}
