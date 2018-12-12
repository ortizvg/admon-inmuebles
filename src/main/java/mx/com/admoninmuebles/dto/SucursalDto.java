package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SucursalDto {
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String nombre;

    
    private String codigoPostal;

    @NotNull
    @Size(min = 1, max = 100)
    private String correo;

    @NotNull
    @Size(min = 1, max = 50)
    private String telefono;
    
    @NotNull
    @Size(min = 1, max = 200)
    private String nombreResponsable;
    
    @NotNull
    @Size(min = 1, max = 1000)
    private String direccionMaps;
    
    private String direccionAsentamientoNombre;
    private String direccionCalle;
    private String direccionNumeroExterior;
    private String direccionNumeroInterior;
    private String direccionEntreCalles;
    private String direccionReferencias;
    private Long direccionAsentamientoId;
    
    private String direccionAsentamientoCodigoPostal;
    private String direccionAsentamientoMunicipioNombre;
    private String direccionAsentamientoMunicipioEstadoNombre;

}
