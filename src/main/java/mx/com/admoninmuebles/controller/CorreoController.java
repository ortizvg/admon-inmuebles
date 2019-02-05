package mx.com.admoninmuebles.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import mx.com.admoninmuebles.constant.PlantillaCorreoConst;
import mx.com.admoninmuebles.dto.CorreoDto;
import mx.com.admoninmuebles.service.CorreoService;

@RestController
@RequestMapping("/api")
public class CorreoController {
	
	 @Autowired
	 private CorreoService correoService;
	 
	 @GetMapping("/sendMail")
	 public String sendMail() {
	       Context datosPlantilla = new Context();
	       datosPlantilla.setVariable("nombre", "Paco");
	       CorreoDto correoDto = new CorreoDto();
	       correoDto.setDe("prueba23@gesco-pls.com");
	       correoDto.setPara("ffcojaviercarrillo@gmail.com");
	       correoDto.setAsunto("Correo de prueba");
	       correoDto.setPlantilla(PlantillaCorreoConst.RECUPERA_CONTRASENIA);
	       correoDto.setDatosPlantilla(datosPlantilla);
	       
	       try {
	    	   correoService.enviarCorreo(correoDto);
	    	   
	    	   return "OK";
	       }catch(Exception e) {
	    	   return e.getMessage();
	       }
	       
	       
	 }

}
