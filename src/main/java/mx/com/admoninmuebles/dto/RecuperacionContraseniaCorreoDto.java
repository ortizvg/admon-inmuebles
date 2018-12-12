package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RecuperacionContraseniaCorreoDto {
	
	@NotNull
	@NotBlank
	private String login;

}
