package mx.com.admoninmuebles.controller;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import mx.com.admoninmuebles.constant.EstatusTicketConst;
import mx.com.admoninmuebles.constant.PrivilegioConst;
import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.TicketDto;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.AreaServicioService;
import mx.com.admoninmuebles.service.TicketService;
import mx.com.admoninmuebles.service.TipoTicketService;
import mx.com.admoninmuebles.service.UsuarioService;

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
	private ServletContext servletContext;

	@PreAuthorize("hasAuthority('" + PrivilegioConst.VER_TICKET + "')")
	@GetMapping(value = "/tickets")
	public String init(final TicketDto ticketDto, final Model model, final HttpServletRequest request) {
		Optional<Long> optId = SecurityUtils.getCurrentUserId();
		if (request.isUserInRole(RolConst.ROLE_SOCIO_BI)) {
			model.addAttribute("tickets", ticketService.findByUsuarioCreadorId(optId.get()));
		} else if (request.isUserInRole(RolConst.ROLE_PROVEEDOR)) {
			model.addAttribute("tickets", ticketService.findByUsuarioAsignadoId(optId.get()));
		} else {
			model.addAttribute("tickets", ticketService.findAll());
		}
		return "ticket/tickets";
	}
 
	@PreAuthorize("hasAuthority('ABRIR_TICKET')")
	@GetMapping(value = "/ticket-crear")
	public String ticketCrear(final TicketDto ticketDto, final HttpSession session) {
		//session.setAttribute("areasServicio", areaServicioService.findAll());
		session.setAttribute("tipoTicket", tipoTicketService.findAll());
		return "ticket/ticket-crear";
	}

	@PreAuthorize("hasAuthority('ABRIR_TICKET')")
	@PostMapping(value = "/ticket")
	public String crearTicket(final Locale locale, @Valid final TicketDto ticketDto,
			final BindingResult bindingResult, @RequestParam("file") MultipartFile file, RedirectAttributes redirect) {
		String showPageFail = "ticket/ticket-crear"; 
		Optional<Long> optId = SecurityUtils.getCurrentUserId();
		if (optId.isPresent()) {
			ticketDto.setUsuarioCreadorId(optId.get());
		}
    	final String MIME_TYPE_JPG = "image/jpeg";
    	final String MIME_TYPE_PDF = "application/pdf";
    	final String MIME_TYPE_PNG = "image/png";
        if (file.isEmpty()) {
        	redirect.addFlashAttribute("messageEmpty","");
            return showPageFail;
        }
        
        try {
			String fileType = file.getContentType();
			if(!(MIME_TYPE_JPG.contains(fileType) || MIME_TYPE_PDF.contains(fileType)
					|| MIME_TYPE_PNG.contains(fileType))) {
				redirect.addFlashAttribute("messageType","");
				return showPageFail;
			}
			ticketDto.setEstatus(EstatusTicketConst.ABIERTO);
			ticketDto.setArchivoEvidencia(IOUtils.toByteArray(file.getInputStream()));
			ticketDto.setTitulo(file.getOriginalFilename());
			ticketService.save(ticketDto);
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
		model.addAttribute("ticketDto", ticketDto);
		if (request.isUserInRole(RolConst.ROLE_SOCIO_BI) || request.isUserInRole(RolConst.ROLE_REP_BI)) {
			vista = "ticket/ticket-detalle";
		} else if (request.isUserInRole(RolConst.ROLE_PROVEEDOR)) {
			vista = "ticket/ticket-aceptar";
		} else {
			vista = "ticket/ticket-asignar";
			//session.setAttribute("proveedores", usuarioService
//					.findByRolesNombreAndAreasServicioId(RolConst.ROLE_PROVEEDOR, ticketDto.getAreaServicioId()));
			session.setAttribute("proveedores", usuarioService
							.findByRolesNombre(RolConst.ROLE_PROVEEDOR));
		}
		return vista;
	}

	@PreAuthorize("hasAuthority('" + PrivilegioConst.ASIGNAR_TICKET + "')")
	@PostMapping(value = "/asignarTicket")
	public String asignar(final Locale locale, @Valid final TicketDto ticketDto, final BindingResult bindingResult) {
		TicketDto ticketDtoTemp = ticketService.findById(ticketDto.getId());
		ticketDtoTemp.setUsuarioAsignadoId(ticketDto.getUsuarioAsignadoId());
		ticketDtoTemp.setEstatus(EstatusTicketConst.ASIGNADO);
		ticketService.save(ticketDtoTemp);
		return "redirect:tickets";
	}

	@PreAuthorize("hasAuthority('" + PrivilegioConst.ACEPTAR_TICKET + "')")
	@GetMapping(value = "/ticket-aceptar/{id}")
	public String aceptar(final @PathVariable long id) {
		TicketDto ticketDtoTemp = ticketService.findById(id);
		ticketDtoTemp.setEstatus(EstatusTicketConst.ACEPTADO);
		ticketService.save(ticketDtoTemp);
		return "redirect:/tickets";
	}
	
	@PreAuthorize("hasAuthority('" + PrivilegioConst.ATENDER_TICKET + "')")
	@GetMapping(value = "/ticket-rechazar/{id}")
	public String rechazar(final @PathVariable long id) {
		TicketDto ticketDtoTemp = ticketService.findById(id);
		ticketDtoTemp.setEstatus(EstatusTicketConst.RECHAZADO);
		ticketDtoTemp.setUsuarioAsignadoId(null);
		ticketDtoTemp.setUsuarioAsignadoNombre(null);
		ticketDtoTemp.setUsuarioAsignadoApellidoPaterno(null);
		ticketDtoTemp.setUsuarioAsignadoApellidoMaterno(null);
		
		ticketService.save(ticketDtoTemp);
		return "redirect:/tickets";
	}

	@PreAuthorize("hasAuthority('" + PrivilegioConst.ASIGNAR_TICKET + "')")
	@GetMapping(value = "/ticket-terminar/{id}")
	public String terminar(final @PathVariable long id) {
		TicketDto ticketDtoTemp = ticketService.findById(id);
		ticketDtoTemp.setEstatus(EstatusTicketConst.ATENDIDO);
		ticketService.save(ticketDtoTemp);
		return "redirect:/tickets";
	}

	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
	@GetMapping(value = "/ticket-eliminar/{id}")
	public String eliminarTicket(final @PathVariable long id) {
		ticketService.delete(id);
		return "redirect:ticket/tickets";
	}
	
	
	@GetMapping(value="/ticket-download/{id}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long id) {
	    TicketDto ticketDto = ticketService.findById(id);
	    String strMediaType = servletContext.getMimeType(ticketDto.getTitulo());
	    MediaType mediaType = MediaType.parseMediaType(strMediaType);
	    ByteArrayResource resource = new ByteArrayResource(ticketDto.getArchivoEvidencia());
	    return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+ticketDto.getTitulo())
                // Content-Type
                .contentType(mediaType) //
                // Content-Lengh
                .contentLength(ticketDto.getArchivoEvidencia().length) //
                .body(resource);
	}

}
