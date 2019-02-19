package mx.com.admoninmuebles.controller;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.ComunicadoDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.ComunicadoService;
import mx.com.admoninmuebles.service.InmuebleService;

@Controller
public class ComunicadoController {
	
	Logger logger = LoggerFactory.getLogger(ComunicadoController.class);

	@Autowired
	private InmuebleService inmuebleService;

	@Autowired
	private ComunicadoService comunicadoService;
	
    @Autowired
    private MessageSource messages;
	
	@PreAuthorize("hasAnyRole('CONTADOR', 'SOCIO_BI')")
	@GetMapping(value = "/reportes/comunicados")
	public String mostrarComunidados(Model model, final HttpServletRequest request) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		
		 if (request.isUserInRole(RolConst.ROLE_SOCIO_BI)) {
			InmuebleDto inmueble = inmuebleService.findBySocioId( usuarioLogueadoId );
			model.addAttribute("comunicados", comunicadoService.buscarPorInmuebleId( inmueble.getId() ) );
	         
	     } else if (request.isUserInRole(RolConst.ROLE_CONTADOR)) {
			model.addAttribute("comunicados", comunicadoService.buscarPorContadorId( usuarioLogueadoId ) );
			model.addAttribute("inmuebles", inmuebleService.findByContadorId( usuarioLogueadoId ) );
	     } 


		return "reportes/comunicados";
	}

	@PreAuthorize("hasRole('CONTADOR')")
	@GetMapping(value = "/reportes/comunicados/carga")
	public String mostrarFormaComunidacodo(Model model, final HttpServletRequest request, final HttpSession session, Locale locale) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();

		if (!model.containsAttribute("comunicado")) {
			ComunicadoDto comunicado = new ComunicadoDto();
			model.addAttribute("comunicado",  comunicado );
        }
		
		session.setAttribute("inmuebles", inmuebleService.findByContadorId(usuarioLogueadoId));

		return "reportes/comunicado-carga";
	}

	@PreAuthorize("hasRole('CONTADOR')")
	@PostMapping(value = "/reportes/comunicados/carga")
	public String guardarComunicado(final HttpServletRequest request, final Locale locale, final Model model,
			@Valid final ComunicadoDto comunicado, final BindingResult bindingResult, RedirectAttributes redirect) {
		
		if (bindingResult.hasErrors()) {
			redirect.addFlashAttribute("comunicado",  comunicado );
			return "reportes/comunicado-carga";
		}
		
		try {
			comunicadoService.guardar( comunicado );
		} catch (BusinessException e) {
			redirect.addFlashAttribute("comunicado",  comunicado );
			redirect.addFlashAttribute("error", messages.getMessage(e.getMessage(), null, locale));
			return "redirect:/reportes/comunicados/carga"; 
		}

		redirect.addFlashAttribute("message", messages.getMessage("comunicados.guardado.exito", null, locale));
		return "redirect:/reportes/comunicados/carga"; 
	}
	
	@PreAuthorize("hasRole('CONTADOR')")
	@GetMapping(value = "/reportes/comunicados/{id}/eliminar")
	public String eliminarComunicado(final @PathVariable long id) {
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		ComunicadoDto comunicadoAEliminar = null;
		
		try {
			comunicadoAEliminar = comunicadoService.buscarPorId( id );
		}catch( BusinessException e) {
			return "error/404";
		}
		
		Collection<ComunicadoDto> comunicados =  comunicadoService.buscarPorContadorId( usuarioLogueadoId );
		
		if(!comunicados.contains( comunicadoAEliminar )) {
			return "error/404";
		}
		
		comunicadoService.eliminar( id );
		return "redirect:/reportes/comunicados";
	}

}
