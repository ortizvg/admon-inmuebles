package mx.com.admoninmuebles.dto;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import mx.com.admoninmuebles.constant.ComunConst;

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
    
    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    @DateTimeFormat(iso = ISO.DATE)
    private Date fechaCreacion;
    private boolean retraso;
    
    private Long inmuebleId;

    public String getNombreSocio() {
    	StringBuffer nombreCompleto = new StringBuffer()
    			.append( this.usuarioCreadorNombre )
    			.append( ComunConst.CADENA_ESPACIO )
    			.append( this.usuarioCreadorApellidoPaterno == null ? ComunConst.CADENA_VACIA : this.usuarioCreadorApellidoPaterno )
    			.append( ComunConst.CADENA_ESPACIO )
    			.append( this.usuarioAsignadoApellidoMaterno == null ? ComunConst.CADENA_VACIA : this.usuarioCreadorApellidoMaterno );
    			
    	return nombreCompleto.toString();

//        return String.format("%s %s %s", usuarioCreadorNombre, usuarioCreadorApellidoPaterno, usuarioCreadorApellidoMaterno);
    }

    public String getNombreProveedor() {
    	StringBuffer nombreCompleto = new StringBuffer()
    			.append( this.usuarioAsignadoNombre == null ? ComunConst.CADENA_VACIA : this.usuarioAsignadoNombre )
    			.append( ComunConst.CADENA_ESPACIO )
    			.append( this.usuarioAsignadoApellidoPaterno == null ? ComunConst.CADENA_VACIA : this.usuarioAsignadoApellidoPaterno )
    			.append( ComunConst.CADENA_ESPACIO )
    			.append( this.usuarioAsignadoApellidoMaterno == null ? ComunConst.CADENA_VACIA : this.usuarioAsignadoApellidoMaterno );
    			
    	return nombreCompleto.toString();
//        return String.format("%s %s %s", usuarioAsignadoNombre, usuarioAsignadoApellidoPaterno, usuarioAsignadoApellidoMaterno);
    }

    public String getDireccion() {
    	StringBuffer direcccion = new StringBuffer()
    			.append( this.usuarioCreadorInmuebleNombre == null ? ComunConst.CADENA_VACIA : this.usuarioCreadorInmuebleNombre )
    			.append( ComunConst.CADENA_ESPACIO )
    			.append( this.usuarioCreadorInmuebleDireccionCalle == null ? ComunConst.CADENA_VACIA : this.usuarioCreadorInmuebleDireccionCalle )
    			.append( ComunConst.CADENA_ESPACIO )
    			.append( this.usuarioCreadorInmuebleDireccionNumeroExterior == null ? ComunConst.CADENA_VACIA : this.usuarioCreadorInmuebleDireccionNumeroExterior )
    			.append( ComunConst.CADENA_ESPACIO )
    			.append( this.usuarioCreadorInmuebleDireccionAsentamientoNombre == null ? ComunConst.CADENA_VACIA : this.usuarioCreadorInmuebleDireccionAsentamientoNombre );
    	
    	return direcccion.toString();
    	
//        return String.format("%s, %s, %s, %s", usuarioCreadorInmuebleNombre, usuarioCreadorInmuebleDireccionCalle, usuarioCreadorInmuebleDireccionNumeroExterior,
//                usuarioCreadorInmuebleDireccionAsentamientoNombre);
    }
    private Collection<CambioTicketDto> cambios;
    
    public String getNombreContador() {
    	StringBuffer nombreCompleto = new StringBuffer()
    			.append( this.usuarioAsignadoNombre == null ? ComunConst.CADENA_VACIA : this.usuarioAsignadoNombre )
    			.append( ComunConst.CADENA_ESPACIO )
    			.append( this.usuarioAsignadoApellidoPaterno == null ? ComunConst.CADENA_VACIA : this.usuarioAsignadoApellidoPaterno )
    			.append( ComunConst.CADENA_ESPACIO )
    			.append( this.usuarioAsignadoApellidoMaterno == null ? ComunConst.CADENA_VACIA : this.usuarioAsignadoApellidoMaterno );
    			
    	return nombreCompleto.toString();
//        return String.format("%s %s %s", usuarioAsignadoNombre, usuarioAsignadoApellidoPaterno, usuarioAsignadoApellidoMaterno);
    }

    public String getNombreAdmin() {
    	StringBuffer nombreCompleto = new StringBuffer()
    			.append( this.usuarioAsignadoNombre == null ? ComunConst.CADENA_VACIA : this.usuarioAsignadoNombre )
    			.append( ComunConst.CADENA_ESPACIO )
    			.append( this.usuarioAsignadoApellidoPaterno == null ? ComunConst.CADENA_VACIA : this.usuarioAsignadoApellidoPaterno )
    			.append( ComunConst.CADENA_ESPACIO )
    			.append( this.usuarioAsignadoApellidoMaterno == null ? ComunConst.CADENA_VACIA : this.usuarioAsignadoApellidoMaterno );
    			
    	return nombreCompleto.toString();
//        return String.format("%s %s %s", usuarioAsignadoNombre, usuarioAsignadoApellidoPaterno, usuarioAsignadoApellidoMaterno);
    }
}
