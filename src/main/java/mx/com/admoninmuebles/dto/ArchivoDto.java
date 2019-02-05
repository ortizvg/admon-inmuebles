package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ArchivoDto {
	
	private String id;
	
	@NotNull
    @Size(min = 1, max = 255)
	private String nombre;
	
    @NotNull
    @Size(min = 1, max = 50)
	private String tipoContenido;
	
    @NotNull
    private byte[] bytes;

}
