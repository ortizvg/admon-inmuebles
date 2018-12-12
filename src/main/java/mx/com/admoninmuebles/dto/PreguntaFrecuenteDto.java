package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PreguntaFrecuenteDto {
    private Long id;

    @NotNull
    @Size(min = 1, max = 500)
    private String pregunta;

    @NotNull
    @Size(min = 1, max = 2000)
    private String respuesta;
    
    private String idioma;

}
