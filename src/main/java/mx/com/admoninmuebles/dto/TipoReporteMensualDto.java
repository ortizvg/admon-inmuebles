package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TipoReporteMensualDto {
	
	private Long id;
	
	@NotNull
	private String descripcion;

}
