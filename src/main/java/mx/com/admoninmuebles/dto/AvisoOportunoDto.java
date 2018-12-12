package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AvisoOportunoDto {

    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String titulo;

    @NotNull
    @Size(min = 1, max = 2000)
    private String descripcion;

    @NotNull
    @Size(min = 1, max = 100)
    private String imagenUrl;

}
