package mx.com.admoninmuebles.controller;

import java.time.LocalDate;
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

import mx.com.admoninmuebles.dto.ReporteMensualDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.MesService;
import mx.com.admoninmuebles.service.ReporteMensualService;
import mx.com.admoninmuebles.service.TipoReporteMensualService;

@Controller
public class ReporteMensualController {

	Logger logger = LoggerFactory.getLogger(ReporteMensualController.class);

	@Autowired
	private InmuebleService inmuebleService;

	@Autowired
	private MesService mesService;

	@Autowired
	private TipoReporteMensualService tipoReporteMensualService;

	@Autowired
	private ReporteMensualService reporteMensualService;
	
    @Autowired
    private MessageSource messages;
	
	@PreAuthorize("hasRole('CONTADOR')")
	@GetMapping(value = "/reportes/reportes-mensuales")
	public String mostrarReportesMesuales(Model model, final HttpServletRequest request) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();

		model.addAttribute("reportesMensuales", reporteMensualService.buscarPorContadorId( usuarioLogueadoId ) );
		model.addAttribute("inmuebles", inmuebleService.findByContadorId( usuarioLogueadoId ) );

		return "reportes/reportes-mensuales";
	}

	@PreAuthorize("hasRole('CONTADOR')")
	@GetMapping(value = "/reportes/reportes-mensuales/carga")
	public String mostrarFormaCargaReporteMesual(Model model, final HttpServletRequest request, final HttpSession session, Locale locale) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();

		if (!model.containsAttribute("reporteMensual")) {
			ReporteMensualDto reporteMensual = new ReporteMensualDto();
			reporteMensual.setAnio(LocalDate.now().getYear());
			model.addAttribute("reporteMensual",  reporteMensual );
        }
		
		session.setAttribute("inmuebles", inmuebleService.findByContadorId(usuarioLogueadoId));
		session.setAttribute("meses", mesService.buscarPorLang( locale.getLanguage() ) );
		session.setAttribute("tiposReporteMensual", tipoReporteMensualService.buscarPorLang( locale.getLanguage() ));

		return "reportes/reporte-mensual-carga";
	}

	@PreAuthorize("hasRole('CONTADOR')")
	@PostMapping(value = "/reportes/reportes-mensuales/carga")
	public String guardarReporteMensual(final HttpServletRequest request, final Locale locale, final Model model,
			@Valid final ReporteMensualDto reporteMensual, final BindingResult bindingResult, RedirectAttributes redirect) {
		logger.info("Inicio de guardado: ");
		if (bindingResult.hasErrors()) {
			return "reportes/reporte-mensual-carga";
		}
		
		if( !org.springframework.http.MediaType.APPLICATION_PDF_VALUE.equalsIgnoreCase( reporteMensual.getReporteArchivo().getContentType() ) ) {
			redirect.addFlashAttribute("error", messages.getMessage("archivo.validacion.mediatype.pdf", null, locale));
			redirect.addFlashAttribute("reporteMensual",  reporteMensual );
			return "redirect:/reportes/reportes-mensuales/carga"; 
		}
		
		if( reporteMensual.getReporteArchivo().getSize() > 1000000 ) {
			redirect.addFlashAttribute("reporteMensual",  reporteMensual );
			redirect.addFlashAttribute("error", messages.getMessage("archivo.validacion.tamanio", null, locale));
			return "redirect:/reportes/reportes-mensuales/carga"; 
		}
			
		reporteMensualService.guardar(reporteMensual);

		redirect.addFlashAttribute("message", messages.getMessage("archivo.guardado.exito", null, locale));
//		return "redirect:/reportes/reportes-mensuales";
		return "redirect:/reportes/reportes-mensuales/carga"; 
	}
	
	@PreAuthorize("hasRole('CONTADOR')")
	@GetMapping(value = "/reportes/reportes-mensuales/{id}/eliminar")
	public String eliminarReporteMensual(final @PathVariable long id) {
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		ReporteMensualDto reporteMensualAEliminar = null;
		
		try {
			reporteMensualAEliminar = reporteMensualService.buscarPorId(id);
		}catch( BusinessException e) {
			return "error/404";
		}
		
		Collection<ReporteMensualDto> reportesMensuales =  reporteMensualService.buscarPorContadorId( usuarioLogueadoId );
		
		if(!reportesMensuales.contains(reporteMensualAEliminar)) {
			return "error/404";
		}
		
		reporteMensualService.eliminar(id);
		return "redirect:/reportes/reportes-mensuales";
	}

}
