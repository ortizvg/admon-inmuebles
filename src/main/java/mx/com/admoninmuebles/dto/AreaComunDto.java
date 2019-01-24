package mx.com.admoninmuebles.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
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
    
    @Digits(integer = 7, fraction = 2)
    private BigDecimal cuotaPorDia;

    private Long inmuebleId;

    private String inmuebleNombre;

}
