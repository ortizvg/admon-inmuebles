package mx.com.admoninmuebles.dto;

import org.thymeleaf.context.Context;

import lombok.Data;

@Data
public class CorreoDto {
	
	private String de;
	private String para;
	private String asunto;
	private String plantilla;
	private String conCopiaPara;
	private Context datosPlantilla;

}
