package mx.com.admoninmuebles.dto;

import java.time.LocalDate;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import mx.com.admoninmuebles.constant.ComunConst;
import mx.com.admoninmuebles.validation.ComparacionFechas;

@Data
@ComparacionFechas
public class NotificacionDto {

    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String titulo;

    @NotNull
    @Size(min = 1, max = 1000)
    private String descripcion;
    
//    @DateTimeFormat(pattern="yyyy-MM-dd") 
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate fechaExposicionInicial;

//    @DateTimeFormat(pattern="yyyy-MM-dd") 
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate fechaExposicionFinal;
    
    private Long inmuebleId;
    private String inmuebleNombre;
    
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioApellidoPaterno;
    private String usuarioApellidoMaterno;
    private String usuarioCorreo;
    
    @DateTimeFormat(pattern="dd-MM-yyyy") 
    private Date fechaModificacion;
    
    private Date fechaCreacion;
    
//    private Long creadoPorId;
//    @Setter(AccessLevel.NONE)
//    private String creadoPorNombre;
//    @Setter(AccessLevel.NONE)
//    private String creadoPorApellidoPaterno;
//    @Setter(AccessLevel.NONE)
//    private String creadoPorApellidoMaterno;
    
    
    
    public String getSocio() {
    	if(usuarioId == null) {
    		return ComunConst.CADENA_VACIA;
    	}
    	return this.usuarioNombre + " " + this.usuarioApellidoPaterno + " " + this.usuarioApellidoMaterno;
    }
    
//    public String getCreadoPor() {
//    	if(creadoPorNombre == null) {
//    		return ComunConst.USUARIO_SISTEMA;
//    	}
//    	return this.creadoPorNombre + " " + this.creadoPorApellidoPaterno + " " + this.creadoPorApellidoMaterno;
//    }
    

}
