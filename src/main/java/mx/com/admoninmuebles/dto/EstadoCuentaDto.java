package mx.com.admoninmuebles.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(includeFieldNames=true)
public class EstadoCuentaDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;

	private String descripcion;

	private String archivoId;
	private String archivoNombre;
	private String archivoTipoContenido;
	private byte[] archivoBytes;
	
	private Long socioId;
	private String socioNombre;
	private String socioApellidoPaterno;
	private String socioApellidoMaterno;
	
	@NotNull
	private Long inmuebleId;
	
	private MultipartFile archivoMP;
	
	public String getSocio() {
		return this.socioNombre + " " + this.socioApellidoPaterno + " " + this.socioApellidoMaterno;
	}
}
