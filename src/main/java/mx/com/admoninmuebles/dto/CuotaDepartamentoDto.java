package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(includeFieldNames=true)
public class CuotaDepartamentoDto {
	
	private Long id;

	private String descripcion;

	private String archivoId;
	private String archivoNombre;
	private String archivoTipoContenido;
	private byte[] archivoBytes;
	
	@NotNull
	private Long inmuebleId;
	private String inmuebleNombre;
	
	private MultipartFile archivoMP;

//	@NotNull
//	private Long socioId;
//	private String socioNombre;
//	private String socioApellidoPaterno;
//	private String socioApellidoMaterno;
//	
//	public String getSocio() {
//		return this.socioNombre + " " + this.socioApellidoPaterno + " " + this.socioApellidoMaterno;
//	}

}
