package mx.com.admoninmuebles.persistence.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tickets")
@Data
@EqualsAndHashCode(callSuper = false)
public class Ticket extends EntidadBase {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
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
    private String estatus;

    //@ManyToOne
    //@JoinColumn(name = "id_area_servicio_fk", referencedColumnName = "id_area_servicio", nullable = false)
    //private AreaServicio areaServicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_creador_fk", nullable = false)
    private Usuario usuarioCreador;

    @ManyToOne
    @JoinColumn(name = "id_usuario_asignado_fk", referencedColumnName = "id_usuario")
    private Usuario usuarioAsignado;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ticket")
    private Collection<CambioTicket> cambios = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_tipo_ticket_fk", referencedColumnName = "id_tipo_ticket", nullable = false)
    private TipoTicket tipoTicket;
    
    @Column(name = "archivo_evidencia", columnDefinition = "BLOB", nullable = true)
    private byte[] archivoEvidencia;
    
    @NotNull
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;
    
    public void addCambioTicket(final CambioTicket cambioTicket) {
        cambios.add(cambioTicket);
        cambioTicket.setTicket(this);
    }

}
