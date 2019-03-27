package mx.com.admoninmuebles.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CambioTicketDto {
	
	private Long id;
	private Long ticketId;
    private UsuarioDto modificadoPor;
    private Date fechaModificacion;
    private String comentario;
    private byte[] archivoEvidencia;
    private String tituloArchivoEvidencia;

}
