package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AreaServicioDto {

    private Long id;

    @NotNull
    @Size(min = 5, max = 50)
    private String nombre;
    
    private boolean activo;

}
