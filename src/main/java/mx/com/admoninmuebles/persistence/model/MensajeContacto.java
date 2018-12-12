package mx.com.admoninmuebles.persistence.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "mensajes_contacto")
@Data
@EqualsAndHashCode(callSuper = false)
public class MensajeContacto extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje_contacto")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    private String nombre;
    
    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false)
    private String correo;

    @Size(min = 0, max = 50)
    @Column(length = 50)
    private String telefono;
    
    @Size(min = 0, max = 50)
    @Column(length = 50)
    private String telefonoAlternativo;

    @NotNull
    @Size(min = 1, max = 4000)
    @Column(length = 4000, columnDefinition = "text", nullable = false)
    private String mensaje;
    
    @OneToOne
    @JoinColumn(name = "id_mensajes_contacto_estatus_fk", nullable = true)
    private MensajeContactoEstatus mensajeContactoEstatus;
    
    @OneToOne(optional=true)
    @JoinColumn(name = "id_sector_fk", nullable = true )
    private Sector sector;
    
//    @OneToOne
//    @JoinColumn(name = "id_zona_fk", nullable = true)
//    private Zona zona;
    
    @OneToOne
    @JoinColumn(name = "id_estado_correo_fk", nullable = true)
    private EstadoCorreo estadoCorreo;
    
//    @NotNull
//    private boolean atendido;

}
