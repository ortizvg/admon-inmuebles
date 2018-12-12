package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AreaComunDto {

    private Long id;

    @NotNull
    @Size(min = 5, max = 50)
    private String nombre;

    @NotNull
    @Size(min = 5, max = 50)
    private String descripcion;

    private Long inmuebleId;

    private String inmuebleNombre;

}
