package mx.com.admoninmuebles.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.ComunicadoService;
import mx.com.admoninmuebles.service.CuotaDepartamentoService;
import mx.com.admoninmuebles.service.EstadoCuentaService;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.ReglamentoService;
import mx.com.admoninmuebles.service.ReporteMensualService;

@Controller
public class ReportesController {
	
	Logger logger = LoggerFactory.getLogger(ReportesController.class);
	
	@Autowired
	private InmuebleService inmuebleService;
	
	@Autowired
	private ReporteMensualService reporteMensualService;
	
	@Autowired
	private ReglamentoService reglamentoService;
	
	@Autowired
	private ComunicadoService comunicadoService;
	
	@Autowired
	private CuotaDepartamentoService cuotaDepartamentoService;
	
	@Autowired
	private EstadoCuentaService estadoCuentaService;
	
	@PreAuthorize("hasAnyRole('CONTADOR', 'SOCIO_BI')")
	@GetMapping(value = "/reportes")
	public String mostrarReportes(Model model, final HttpServletRequest request) {

		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		
		InmuebleDto inmueble = inmuebleService.findBySocioId( usuarioLogueadoId );

		model.addAttribute("reporteMensual", reporteMensualService.buscarReciente5PorSocioId( inmueble.getId() ) );
		model.addAttribute("reportesMensuales", reporteMensualService.buscarPorInmuebleId( inmueble.getId() ) );
		model.addAttribute("reglamentos", reglamentoService.buscarPorInmuebleId( inmueble.getId() ) );
		model.addAttribute("comunicados", comunicadoService.buscarPorInmuebleId( inmueble.getId() ) );
		model.addAttribute("cuotasDepartamento", cuotaDepartamentoService.buscarPorInmuebleId( inmueble.getId() ) );
//		model.addAttribute("estadoCuenta", estadoCuentaService.buscarRecientePorSocioId( usuarioLogueadoId ) );
		model.addAttribute("estadosCuenta", estadoCuentaService.buscarPorSocioId( usuarioLogueadoId ) );

		return "reportes/reportes";
	}

}
