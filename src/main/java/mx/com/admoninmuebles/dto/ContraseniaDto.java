package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import mx.com.admoninmuebles.validation.ContraseniaConfirmacion;

@Data
@ContraseniaConfirmacion
public class ContraseniaDto {
	
	@NotNull
	protected String token;
	
	@NotNull
    @Size(min = 6, max = 100)
	protected String contrasenia;
	
	@NotNull
    @Size(min = 6, max = 100)
	protected String confirmacionContrasenia;
	
}
