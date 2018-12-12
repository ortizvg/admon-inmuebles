package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class TipoTicketDto {

    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String nombre;

}
