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

import mx.com.admoninmuebles.dto.CuotaDepartamentoDto;
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
	
	@PreAuthorize("hasRole('CONTADOR')")
	@GetMapping(value = "/reportes/cuotas-departamento")
	public String mostrarCuotasDepartamento(Model model, final HttpServletRequest request) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();

		model.addAttribute("cuotasDepartamento", cuotaDepartamentoService.buscarPorContadorId( usuarioLogueadoId ) );
		model.addAttribute("inmuebles", inmuebleService.findByContadorId( usuarioLogueadoId ) );

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
			return "reportes/cuota-departamento-carga";
		}
		
		if( !org.springframework.http.MediaType.APPLICATION_PDF_VALUE.equalsIgnoreCase( cuotaDepartamento.getArchivoMP().getContentType() ) ) {
			redirect.addFlashAttribute("error", messages.getMessage("archivo.validacion.mediatype.pdf", null, locale));
			redirect.addFlashAttribute("cuotaDepartamento",  cuotaDepartamento );
			return "redirect:/reportes/cuotas-departamento/carga"; 
		}
		
		if( cuotaDepartamento.getArchivoMP().getSize() > 1000000 ) {
			redirect.addFlashAttribute("cuotaDepartamento",  cuotaDepartamento );
			redirect.addFlashAttribute("error", messages.getMessage("archivo.validacion.tamanio", null, locale));
			return "redirect:/reportes/cuotas-departamento/carga"; 
		}
			
		cuotaDepartamentoService.guardar(cuotaDepartamento);

		redirect.addFlashAttribute("message", messages.getMessage("archivo.guardado.exito", null, locale));
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
