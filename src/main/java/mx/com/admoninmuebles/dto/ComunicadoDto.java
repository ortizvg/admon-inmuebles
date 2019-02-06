package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(includeFieldNames=true)
public class ComunicadoDto {
	
	private Long id;

	private String descripcion;

	private Long archivoId;
	private String archivoNombre;
	private String archivoTipoContenido;
	private byte[] archivoBytes;

	@NotNull
	private Long inmuebleId;
	private Long inmuebleNombre;

}
