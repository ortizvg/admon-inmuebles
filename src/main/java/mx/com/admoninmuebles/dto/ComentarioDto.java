package mx.com.admoninmuebles.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import mx.com.admoninmuebles.persistence.model.Usuario;

@Data
public class ComentarioDto {
	
	private Long id;

    private String comentario;
    
    private UsuarioDto modificadoPor;
    
    private Date fechaModificacion;

}
