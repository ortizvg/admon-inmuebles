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
import mx.com.admoninmuebles.dto.CuotaDepartamentoDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.CuotaDepartamentoService;
import mx.com.admoninmuebles.service.InmuebleService;

@Controller
public class CuotaDepartamentoController {
	
	Logger logger = LoggerFactory.getLogger(CuotaDepartamentoController.class);

	@Autowired
	private InmuebleService inmuebleService;

	@Autowired
	private CuotaDepartamentoService cuotaDepartamentoService;
	
    @Autowired
    private MessageSource messages;
	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'CONTADOR', 'SOCIO_BI')")
	@GetMapping(value = "/reportes/cuotas-departamento")
	public String mostrarCuotasDepartamento(Model model, final HttpServletRequest request) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		
		if (request.isUserInRole(RolConst.ROLE_SOCIO_BI)) {
			InmuebleDto inmueble = inmuebleService.findBySocioId( usuarioLogueadoId );
			model.addAttribute("cuotasDepartamento", cuotaDepartamentoService.buscarPorInmuebleId( inmueble.getId() ) );
			
	    } else if (request.isUserInRole(RolConst.ROLE_CONTADOR)) {
			model.addAttribute("cuotasDepartamento", cuotaDepartamentoService.buscarPorContadorId( usuarioLogueadoId ) );
			model.addAttribute("inmuebles", inmuebleService.findByContadorId( usuarioLogueadoId ) );
	    } else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
			model.addAttribute("cuotasDepartamento", cuotaDepartamentoService.buscarPorAdminBiId( usuarioLogueadoId ) );
	    } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
			model.addAttribute("cuotasDepartamento", cuotaDepartamentoService.buscarPorAdminZonaId( usuarioLogueadoId ) );
	    } else if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
			model.addAttribute("cuotasDepartamento", cuotaDepartamentoService.buscarTodo() );
	    } 


		return "reportes/cuotas-departamento";
	}

	@PreAuthorize("hasRole('CONTADOR')")
	@GetMapping(value = "/reportes/cuotas-departamento/carga")
	public String mostrarFormaCargaCuotaDepartamento(Model model, final HttpServletRequest request, final HttpSession session, Locale locale) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();

		if (!model.containsAttribute("cuotaDepartamento")) {
			CuotaDepartamentoDto cuotaDepartamento = new CuotaDepartamentoDto();
			model.addAttribute("cuotaDepartamento",  cuotaDepartamento );
        }
		
		session.setAttribute("inmuebles", inmuebleService.findByContadorId(usuarioLogueadoId));

		return "reportes/cuota-departamento-carga";
	}

	@PreAuthorize("hasRole('CONTADOR')")
	@PostMapping(value = "/reportes/cuotas-departamento/carga")
	public String guardarCuotaDepartamento(final HttpServletRequest request, final Locale locale, final Model model,
			@Valid final CuotaDepartamentoDto cuotaDepartamento, final BindingResult bindingResult, RedirectAttributes redirect) {
		
		if (bindingResult.hasErrors()) {
			redirect.addFlashAttribute("cuotaDepartamento",  cuotaDepartamento );
			return "reportes/cuota-departamento-carga";
		}
		
		try {
			cuotaDepartamentoService.guardar(cuotaDepartamento);
		} catch (BusinessException e) {
			redirect.addFlashAttribute("cuotaDepartamento",  cuotaDepartamento );
			redirect.addFlashAttribute("error", messages.getMessage(e.getMessage(), null, locale));
			return "redirect:/reportes/cuotas-departamento/carga"; 
		}
			

		redirect.addFlashAttribute("message", messages.getMessage("cuota.departamento.guardado.exito", null, locale));
		return "redirect:/reportes/cuotas-departamento/carga"; 
	}
	
	@PreAuthorize("hasRole('CONTADOR')")
	@GetMapping(value = "/reportes/cuotas-departamento/{id}/eliminar")
	public String eliminarCuotaDepartamento(final @PathVariable long id) {
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		CuotaDepartamentoDto cuotaDepartamentoAEliminar = null;
		
		try {
			cuotaDepartamentoAEliminar = cuotaDepartamentoService.buscarPorId(id);
		}catch( BusinessException e) {
			return "error/404";
		}
		
		Collection<CuotaDepartamentoDto> cuotasDepartamento =  cuotaDepartamentoService.buscarPorContadorId( usuarioLogueadoId );
		
		if(!cuotasDepartamento.contains( cuotaDepartamentoAEliminar )) {
			return "error/404";
		}
		
		cuotaDepartamentoService.eliminar(id);
		return "redirect:/reportes/cuotas-departamento";
	}

}
