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
import mx.com.admoninmuebles.dto.EstadoCuentaDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.EstadoCuentaService;
import mx.com.admoninmuebles.service.InmuebleService;

@Controller
public class EstadoCuentaController {
	
	Logger logger = LoggerFactory.getLogger(EstadoCuentaController.class);

	@Autowired
	private InmuebleService inmuebleService;

	@Autowired
	private EstadoCuentaService estadoCuentaService;
	
    @Autowired
    private MessageSource messages;
	
	@PreAuthorize("hasAnyRole('CONTADOR', 'SOCIO_BI')")
	@GetMapping(value = "/reportes/estados-cuenta")
	public String mostrarestadosCuenta(Model model, final HttpServletRequest request) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		
		if (request.isUserInRole(RolConst.ROLE_SOCIO_BI)) {
			model.addAttribute("estadosCuenta", estadoCuentaService.buscarPorSocioId( usuarioLogueadoId ) );
	         
	    } else if (request.isUserInRole(RolConst.ROLE_CONTADOR)) {
			model.addAttribute("estadosCuenta", estadoCuentaService.buscarPorContadorId( usuarioLogueadoId ) );
			model.addAttribute("inmuebles", inmuebleService.findByContadorId( usuarioLogueadoId ) );
	    } 


		return "reportes/estados-cuenta";
	}

	@PreAuthorize("hasRole('CONTADOR')")
	@GetMapping(value = "/reportes/estados-cuenta/carga")
	public String mostrarFormaCargaestadoCuenta(Model model, final HttpServletRequest request, final HttpSession session, Locale locale) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();

		if (!model.containsAttribute("estadoCuenta")) {
			EstadoCuentaDto estadoCuenta = new EstadoCuentaDto();
			model.addAttribute("estadoCuenta",  estadoCuenta );
        }
		
		session.setAttribute("inmuebles", inmuebleService.findByContadorId(usuarioLogueadoId));

		return "reportes/estado-cuenta-carga";
	}

	@PreAuthorize("hasRole('CONTADOR')")
	@PostMapping(value = "/reportes/estados-cuenta/carga")
	public String guardarestadoCuenta(final HttpServletRequest request, final Locale locale, final Model model,
			@Valid final EstadoCuentaDto estadoCuenta, final BindingResult bindingResult, RedirectAttributes redirect) {
		
		if (bindingResult.hasErrors()) {
			redirect.addFlashAttribute("estadoCuenta",  estadoCuenta );
			return "reportes/estado-cuenta-carga";
		}
		
		try {
			estadoCuentaService.guardar(estadoCuenta);
		} catch (BusinessException e) {
			redirect.addFlashAttribute("estadoCuenta",  estadoCuenta );
			redirect.addFlashAttribute("error", messages.getMessage(e.getMessage(), null, locale));
			return "redirect:/reportes/cuotas-departamento/carga"; 
		}
			

		redirect.addFlashAttribute("message", messages.getMessage("estado.cuenta.guardado.exito", null, locale));
		return "redirect:/reportes/estados-cuenta/carga"; 
	}
	
	@PreAuthorize("hasRole('CONTADOR')")
	@GetMapping(value = "/reportes/estados-cuenta/{id}/eliminar")
	public String eliminarestadoCuenta(final @PathVariable long id) {
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		EstadoCuentaDto estadoCuentaAEliminar = null;
		
		try {
			estadoCuentaAEliminar = estadoCuentaService.buscarPorId(id);
		}catch( BusinessException e) {
			return "error/404";
		}
		
		Collection<EstadoCuentaDto> estadosCuenta =  estadoCuentaService.buscarPorContadorId( usuarioLogueadoId );
		
		if(!estadosCuenta.contains( estadoCuentaAEliminar )) {
			return "error/404";
		}
		
		estadoCuentaService.eliminar(id);
		return "redirect:/reportes/estados-cuenta";
	}


}
