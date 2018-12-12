package mx.com.admoninmuebles.persistence.model;

import java.io.Serializable;
import java.util.Collection;

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
@Table(name = "asentamientos")
@Data
@EqualsAndHashCode(callSuper = false)
public class Asentamiento implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asentamiento")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    private String nombre;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    private String codigoPostal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_asentamiento_fk")
    private TipoAsentamiento tipoAsentamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_municipio_fk")
    private Municipio municipio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_zona_fk")
    public Zona zona;

    @OneToMany(mappedBy = "asentamiento")
    private Collection<Direccion> direcciones;

}
