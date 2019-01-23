package mx.com.admoninmuebles.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class TicketDto {
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    private String titulo;

    @NotNull
    @Size(min = 1, max = 4000)
    @Column(length = 4000, columnDefinition = "text", nullable = false)
    private String descripcion;

    @NotNull
    private Long areaServicioId;
    private Long tipoTicketId;
    private String areaServicioNombre;
    private String tipoTicketNombre;
    private String estatus;
    private Long usuarioCreadorId;
    private String usuarioCreadorNombre;
    private String usuarioCreadorApellidoPaterno;
    private String usuarioCreadorApellidoMaterno;
    private String usuarioCreadorInmuebleNombre;
    private String usuarioCreadorInmuebleDireccionCalle;
    private String usuarioCreadorInmuebleDireccionNumeroExterior;
    private String usuarioCreadorInmuebleDireccionAsentamientoNombre;
    private Long usuarioAsignadoId;
    private String usuarioAsignadoNombre;
    private String usuarioAsignadoApellidoPaterno;
    private String usuarioAsignadoApellidoMaterno;
    private byte[] archivoEvidencia;

    public String getNombreSocio() {
        return String.format("%s %s %s", usuarioCreadorNombre, usuarioCreadorApellidoPaterno, usuarioCreadorApellidoMaterno);
    }

    public String getNombreProveedor() {
        return String.format("%s %s %s", usuarioAsignadoNombre, usuarioAsignadoApellidoPaterno, usuarioAsignadoApellidoMaterno);
    }

    public String getDireccion() {
        return String.format("%s, %s, %s, %s", usuarioCreadorInmuebleNombre, usuarioCreadorInmuebleDireccionCalle, usuarioCreadorInmuebleDireccionNumeroExterior,
                usuarioCreadorInmuebleDireccionAsentamientoNombre);
    }

}
