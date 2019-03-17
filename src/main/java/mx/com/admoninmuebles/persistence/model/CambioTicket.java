package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "cambios_ticket")
@Data
@EqualsAndHashCode(callSuper = false)
public class CambioTicket extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cambio_ticket")
    private Long id;

    @NotNull
    @Size(min = 1, max = 4000)
    @Column(length = 4000, columnDefinition = "text", nullable = false)
    private String comentario;

//    @NotNull
//    @Size(min = 1, max = 50)
//    @Column(length = 50, unique = true, nullable = false)
//    private String evidenciaImagenUrl;
    
    @Column(name = "archivo_evidencia", columnDefinition = "BLOB", nullable = true)
    private byte[] archivoEvidencia;

    @Column(length = 20, name = "titulo_archivo_evidencia", nullable = true)
    private String tituloArchivoEvidencia;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ticket_fk", referencedColumnName = "id_ticket", nullable = false)
    private Ticket ticket;
}
