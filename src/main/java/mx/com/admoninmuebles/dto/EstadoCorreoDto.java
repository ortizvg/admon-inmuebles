package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class EstadoCorreoDto {

	private Long id;

	@NotNull
	@Size(min = 1, max = 50)
	private String nombre;

	@NotNull
	@Size(min = 1, max = 100)
	private String correoPrincipal;

	@NotNull
	@Size(min = 1, max = 100)
	private String correoSecundario;

}
