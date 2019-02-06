package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MesDto {
	
	private Long id;
	
	@NotNull
	private String descripcion;

}
