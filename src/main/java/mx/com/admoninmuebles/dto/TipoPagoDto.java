package mx.com.admoninmuebles.dto;

import lombok.Data;

@Data
public class TipoPagoDto {
	
    private Long id;
    private String name;
    private String descripction;
    private String langg;
    private boolean activo;


}
