package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "sucursales")
@Data
@EqualsAndHashCode(callSuper = false)
public class Sucursal extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, unique = true, nullable = false)
    private String nombre;

    @OneToOne
    @JoinColumn(name = "id_direccion_fk", nullable = false)
    private Direccion direccion;

//    @NotNull
//    @Size(min = 0, max = 1000)
//    @Column(length = 1000, columnDefinition = "text", nullable = false)
//    private String referencias;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100,  nullable = false)
    private String correo;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50,  nullable = false)
    private String telefono;
    
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(length = 1000, columnDefinition = "text", nullable = false)
    private String direccionMaps;
    
    @NotNull
    @Size(min = 1, max = 200)
    @Column(length = 200,  nullable = false)
    private String nombreResponsable;
}
