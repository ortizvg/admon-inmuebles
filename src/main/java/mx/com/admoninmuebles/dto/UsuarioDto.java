package mx.com.admoninmuebles.dto;

import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import lombok.Data;

@Data
public class UsuarioDto {

    private Long id;

    @NotNull
    @Size(min = 6, max = 25)
    private String username;

    @NotNull
    @Size(min = 1, max = 100)
    private String nombre;

    @NotNull
    @Size(max = 100)
    private String apellidoPaterno;

    private String apellidoMaterno;

    @NotNull
    @Email
    @Size(max = 100)
    private String correo;

    @Size(max = 15)
    private String telefonoFijo;

    @Size(max = 15)
    private String telefonoOficina;

    @Size(max = 15)
    private String telefonoMovil;

    @Size(max = 15)
    private String telefonoAlternativo;

    @Size(max = 100)
    private String facebook;

    @Size(max = 100)
    private String twiter;

    @Size(max = 100)
    private String youtube;

    @Size(max = 1000)
    private String googleMapsDir;

    @Size(max = 256)
    private String fotoUrl;

    private boolean cuentaExpirada;

    private boolean cuentaBloqueada;

    private boolean credencialesExpiradas;

    private boolean activo;

    private String identificador;

    private String contrasenia;
    
    private String referenciaPagoSocio;
    
    private String cuentaPagoSocio;
    
    @Digits(integer = 7, fraction = 2)
    private BigDecimal coutaMensualPagoSocio;
    
    private String datosDomicilio;
    
    private MultipartFile imagen;
    
    private Collection<RolDto> roles;
    private Long rolSeleccionado;
    private String zonaSeleccionado;
    private Long coloniaSeleccionado;
    
    private Long inmuebleId;
    private String inmuebleNombre;
    private Long inmuebleDireccionAsentamientoId;
    private String inmuebleDireccionAsentamientoZonaCodigo;
    

    public String getNombreCompleto() {
        return String.format("%s %s %s", nombre, apellidoPaterno, apellidoMaterno);
    }

}
