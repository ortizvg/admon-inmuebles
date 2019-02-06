package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(includeFieldNames=true)
public class ReporteMensualDto {
	
	private Long id;

	private String descripcion;

	@NotNull
	private Integer anio;

	@NotNull
	private Long mesId;
	private String mesDescripcion;

	@NotNull
	private Long tipoReporteMensualId;
	private String tipoReporteMensualDescripcion;

	private Long archivoId;
	private String archivoNombre;
	private String archivoTipoContenido;
	private byte[] archivoBytes;

	@NotNull
	private Long inmuebleId;
	private Long inmuebleNombre;

}
