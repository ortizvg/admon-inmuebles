package mx.com.admoninmuebles.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import mx.com.admoninmuebles.validation.ContactoTelefonos;
import mx.com.admoninmuebles.validation.ContraseniaConfirmacion;

@Data
@ContactoTelefonos
public class MensajeContactoDto {
	
    private Long id;

    @NotNull
    private String nombre;
    
    @NotNull
    private String correo;

    private String telefono;
    
    private String telefonoAlternativo;

    @NotNull
    private String mensaje;
    
    private Long MensajeContactoEstatusId;
    
    private String MensajeContactoEstatusNombre;
    
    private Long sectorId;
    
    private String sectorNombre;
    
    private String zonaCodigo;
    
    private String zonaNombre;
    
    private Long estadoCorreoId;
    
    private String estadoCorreoNombre;
    
//    private boolean atendido;

}
