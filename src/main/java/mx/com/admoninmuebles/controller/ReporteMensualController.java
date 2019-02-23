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

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.ReporteMensualDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.ReporteMensualService;
import mx.com.admoninmuebles.service.TipoReporteMensualService;

@Controller
public class ReporteMensualController {

	Logger logger = LoggerFactory.getLogger(ReporteMensualController.class);

	@Autowired
	private InmuebleService inmuebleService;

//	@Autowired
//	private MesService mesService;

	@Autowired
	private TipoReporteMensualService tipoReporteMensualService;

	@Autowired
	private ReporteMensualService reporteMensualService;
	
    @Autowired
    private MessageSource messages;
	
	@PreAuthorize("hasAnyRole('CONTADOR', 'SOCIO_BI')")
	@GetMapping(value = "/reportes/reportes-mensuales")
	public String mostrarReportesMesuales(Model model, final HttpServletRequest request) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		
		 if (request.isUserInRole(RolConst.ROLE_SOCIO_BI)) {
			 InmuebleDto inmueble = inmuebleService.findBySocioId( usuarioLogueadoId );
			 model.addAttribute("reportesMensuales", reporteMensualService.buscarActualPorInmuebleId( inmueble.getId() ) );
         } else if (request.isUserInRole(RolConst.ROLE_CONTADOR)) {
			model.addAttribute("reportesMensuales", reporteMensualService.buscarActualPorContadorId( usuarioLogueadoId ) );
         } 
		 
		 
		return "reportes/reportes-mensuales";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR', 'SOCIO_BI')")
	@GetMapping(value = "/reportes/reportes-mensuales/historico")
	public String mostrarHistoricoReportesMesuales(Model model, final HttpServletRequest request) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		 if (request.isUserInRole(RolConst.ROLE_SOCIO_BI)) {
			 InmuebleDto inmueble = inmuebleService.findBySocioId( usuarioLogueadoId );
			 model.addAttribute("reportesMensuales", reporteMensualService.buscarPorInmuebleId( inmueble.getId() ) );
             
         } else if (request.isUserInRole(RolConst.ROLE_CONTADOR)) {
			model.addAttribute("reportesMensuales", reporteMensualService.buscarPorContadorId( usuarioLogueadoId ) );
         } else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
			model.addAttribute("reportesMensuales", reporteMensualService.buscarPorAdminBiId( usuarioLogueadoId ) );
         } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
			model.addAttribute("reportesMensuales", reporteMensualService.buscarPorAdminZonaId( usuarioLogueadoId ) );
         } else if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
			model.addAttribute("reportesMensuales", reporteMensualService.buscarTodo() );
         } 

		return "reportes/reportes-mensuales-historico";
	}

	@PreAuthorize("hasRole('CONTADOR')")
	@GetMapping(value = "/reportes/reportes-mensuales/carga")
	public String mostrarFormaCargaReporteMesual(Model model, final HttpServletRequest request, final HttpSession session, Locale locale) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();

		if (!model.containsAttribute("reporteMensual")) {
			ReporteMensualDto reporteMensual = new ReporteMensualDto();
			reporteMensual.setAnio(LocalDate.now().getYear());
			reporteMensual.setMesId(  Long.valueOf( LocalDate.now().getMonthValue() - 1 ) ); 
			model.addAttribute("reporteMensual",  reporteMensual );
        }
		
		session.setAttribute("inmuebles", inmuebleService.findByContadorId(usuarioLogueadoId));
//		session.setAttribute("meses", mesService.buscarPorLang( locale.getLanguage() ) );
		session.setAttribute("tiposReporteMensual", tipoReporteMensualService.buscarPorLang( locale.getLanguage() ));

		return "reportes/reporte-mensual-carga";
	}

	@PreAuthorize("hasRole('CONTADOR')")
	@PostMapping(value = "/reportes/reportes-mensuales/carga")
	public String guardarReporteMensual(final HttpServletRequest request, final Locale locale, final Model model,
			@Valid final ReporteMensualDto reporteMensual, final BindingResult bindingResult, RedirectAttributes redirect) {
		
		logger.info("Inicio de guardado: ");
		if (bindingResult.hasErrors()) {
			redirect.addFlashAttribute("reporteMensual",  reporteMensual );
			return "reportes/reporte-mensual-carga";
		}
			
		try {
			reporteMensualService.guardar(reporteMensual);
		} catch (BusinessException e) {
			redirect.addFlashAttribute("reporteMensual",  reporteMensual );
			redirect.addFlashAttribute("error", messages.getMessage(e.getMessage(), null, locale));
			return "redirect:/reportes/reportes-mensuales/carga"; 
		}

		redirect.addFlashAttribute("message", messages.getMessage("reporte.mensual.guardado.exito", null, locale));
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
	
	@PreAuthorize("hasRole('CONTADOR')")
	@GetMapping(value = "/reportes/reportes-mensuales/historico/{id}/eliminar")
	public String eliminarReporteMensualHistorico(final @PathVariable long id) {
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
		return "redirect:/reportes/reportes-mensuales/historico";
	}

}
