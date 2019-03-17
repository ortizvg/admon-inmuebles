package mx.com.admoninmuebles.dto;

import lombok.Data;

@Data
public class CambioTicketDto {
	
	private Long id;
	private Long ticketId;
    private String comentario;
    private byte[] archivoEvidencia;
    private String tituloArchivoEvidencia;

}
