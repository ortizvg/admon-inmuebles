package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(includeFieldNames=true)
public class EstadoCuentaDto {

	private Long id;

	private String descripcion;

	private Long archivoId;
	private String archivoNombre;
	private String archivoTipoContenido;
	private byte[] archivoBytes;
	
	@NotNull
	private Long socioId;
	private String socioNombre;
	private String socioApellidoPaterno;
	private String socioApellidoMaterno;
	
	public String getSocio() {
		return this.socioNombre + " " + this.socioApellidoPaterno + " " + this.socioApellidoMaterno;
	}
}
