package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "telefonos")
@Data
@EqualsAndHashCode(callSuper = false)
public class Telefono extends EntidadBase {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefono")
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String numero;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_telefono", referencedColumnName = "id_tipo_telefono", nullable = false)
    private TipoTelefono tipoTelefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;
}
