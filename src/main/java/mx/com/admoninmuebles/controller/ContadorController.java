package mx.com.admoninmuebles.controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.PagoService;

@Controller
public class ContadorController {
	
	
	Logger logger = LoggerFactory.getLogger(ContadorController.class);
	
	@Autowired
	private PagoService pagoService;
	
	@GetMapping("/contador")
	public String mostrarInicio(Model model) {
		Long idContador = SecurityUtils.getCurrentUserId().get();
		Collection<PagoDto> pagos = pagoService.buscarPorContador( idContador );
		model.addAttribute("pagos", pagos);
		
		return "contador/inicio";
	}

}