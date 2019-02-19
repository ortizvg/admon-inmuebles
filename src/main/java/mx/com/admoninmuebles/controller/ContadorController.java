package mx.com.admoninmuebles.controller;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.NotificacionService;
import mx.com.admoninmuebles.service.PagoService;

@Controller
public class ContadorController {
	
	
	Logger logger = LoggerFactory.getLogger(ContadorController.class);
	
	@Autowired
	private NotificacionService notificacionService;
	
	@Autowired
	private PagoService pagoService;
	
	@GetMapping("/contador")
	public String mostrarInicio(Model model,  final HttpSession session) {
		Long idContador = SecurityUtils.getCurrentUserId().get();
		Collection<PagoDto> pagos = pagoService.buscarPorContador( idContador );
		model.addAttribute("pagos", pagos);
	    session.setAttribute("notificaciones", notificacionService.findByUserIdNotExpired( idContador ));
		
		return "contador/inicio";
	}

}
