package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "direcciones")
@Data
@EqualsAndHashCode(callSuper = false)
public class Direccion extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_direccion")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(length = 100, nullable = true)
    private String calle;

    @NotNull
    @Size(max = 50)
    @Column(name = "numero_exterior", length = 50, nullable = true)
    private String numeroExterior;

    @Size(min = 0, max = 50)
    @Column(length = 50)
    private String numeroInterior;

    @Size(max = 200)
    @Column(length = 200)
    private String entreCalles;

    @Size(max = 100)
    @Column(length = 100)
    private String referencias;

    @ManyToOne
    @JoinColumn(name = "id_asentamiento_fk", nullable = false)
    private Asentamiento asentamiento;

}
