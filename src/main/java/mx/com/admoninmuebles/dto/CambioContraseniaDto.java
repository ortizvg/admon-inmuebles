package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CambioContraseniaDto {
	
    @NotNull(message = "{cambiocontrasenia.error.contraseniaanterior.requerida}")
    @Size(min = 6, max = 100)
	private String contraseniaAnterior;
    
    @NotEmpty(message = "{cambiocontrasenia.error.contrasenianueva.requerida}")
    @Size(min = 6, max = 100)
	private String contraseniaNueva;
    
    @NotNull(message = "{cambiocontrasenia.error.contraseniaconfirmacion.requerida}")
    @Size(min = 6, max = 100)
	private String contraseniaConfirmacion;
	
	

}
