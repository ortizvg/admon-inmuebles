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
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.ReglamentoDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.ReglamentoService;

@Controller
public class ReglamentoController {
	
	Logger logger = LoggerFactory.getLogger(ReglamentoController.class);

	@Autowired
	private InmuebleService inmuebleService;

	@Autowired
	private ReglamentoService reglamentoService;
	
    @Autowired
    private MessageSource messages;
	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'SOCIO_BI')")
	@GetMapping(value = "/reportes/reglamentos")
	public String mostrarReglamentos(Model model, final HttpServletRequest request) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		
		 if (request.isUserInRole(RolConst.ROLE_SOCIO_BI)) {
			InmuebleDto inmueble = inmuebleService.findBySocioId( usuarioLogueadoId );
			model.addAttribute("reglamentos", reglamentoService.buscarPorInmuebleId( inmueble.getId() ) );
             
         } else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
			model.addAttribute("reglamentos", reglamentoService.buscarPorAdminBiId( usuarioLogueadoId ) );
			model.addAttribute("inmuebles", inmuebleService.findByAdminBiId( usuarioLogueadoId ) );
         } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
 			model.addAttribute("reglamentos", reglamentoService.buscarPorAdminZonaId( usuarioLogueadoId ) );
         }else if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
 			model.addAttribute("reglamentos", reglamentoService.buscarTodo() );
         }  
		 
//         else if (request.isUserInRole(RolConst.ROLE_CONTADOR)) {
// 			model.addAttribute("reglamentos", reglamentoService.buscarPorContadorId( usuarioLogueadoId ) );
// 			model.addAttribute("inmuebles", inmuebleService.findByContadorId( usuarioLogueadoId ) );
//          } 


		return "reportes/reglamentos";
	}

	@PreAuthorize("hasRole('ADMIN_BI')")
	@GetMapping(value = "/reportes/reglamentos/carga")
	public String mostrarFormaCargaReglamento(Model model, final HttpServletRequest request, final HttpSession session, Locale locale) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();

		if (!model.containsAttribute("reglamento")) {
			ReglamentoDto reglamento = new ReglamentoDto();
			model.addAttribute("reglamento",  reglamento );
        }
		
		session.setAttribute("inmuebles", inmuebleService.findByAdminBiId(usuarioLogueadoId));

		return "reportes/reglamento-carga";
	}

	@PreAuthorize("hasRole('ADMIN_BI')")
	@PostMapping(value = "/reportes/reglamentos/carga")
	public String guardarReglamento(final HttpServletRequest request, final Locale locale, final Model model,
			@Valid final ReglamentoDto reglamento, final BindingResult bindingResult, RedirectAttributes redirect) {
		
		if (bindingResult.hasErrors()) {
			redirect.addFlashAttribute("reglamento",  reglamento );
			return "reportes/reglamento-carga";
		}
		
		try {
			reglamentoService.guardar( reglamento );
		} catch (BusinessException e) {
			redirect.addFlashAttribute("reglamento",  reglamento );
			redirect.addFlashAttribute("error", messages.getMessage(e.getMessage(), null, locale));
			return "redirect:/reportes/reglamentos/carga"; 
		}

		redirect.addFlashAttribute("message", messages.getMessage("reglamentos.guardado.exito", null, locale));
		return "redirect:/reportes/reglamentos/carga"; 
	}
	
	@PreAuthorize("hasRole('ADMIN_BI')")
	@GetMapping(value = "/reportes/reglamentos/{id}/eliminar")
	public String eliminarReglamento(final @PathVariable long id) {
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		ReglamentoDto reglamentoAEliminar = null;
		
		try {
			reglamentoAEliminar = reglamentoService.buscarPorId(id);
		}catch( BusinessException e) {
			return "error/404";
		}
		
		Collection<ReglamentoDto> reglamentos =  reglamentoService.buscarPorAdminBiId( usuarioLogueadoId );
		
		if(!reglamentos.contains( reglamentoAEliminar )) {
			return "error/404";
		}
		
		reglamentoService.eliminar(id);
		return "redirect:/reportes/reglamentos";
	}


}
