package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ColoniaDto {
    private Long id;
    private String nombre;
    @NotNull
    private String codigoPostal;
    private String zonaCodigo;
    private String zonaNombre;
}
