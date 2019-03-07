package mx.com.admoninmuebles.dto;


import lombok.Data;

@Data
public class MensajeContactoEstatusDto {
	
	private Long id;
    private String nombre;
    private boolean activo;

}
