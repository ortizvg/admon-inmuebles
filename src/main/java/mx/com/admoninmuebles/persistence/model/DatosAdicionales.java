package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "datos_adicionales")
@Data
@EqualsAndHashCode(callSuper = false)
public class DatosAdicionales extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_datos_adicionales")
    private Long id;

    @Size(max = 200)
    @Column(length = 200, nullable = true)
    private String nombreRepresentante;

    @NotNull
    @Size(max = 200)
    @Column(length = 200, nullable = true)
    private String razonSocial;

    @NotNull
    @Size(max = 13)
    @Column(length = 13, nullable = true)
    private String rfc;

    @Size(max = 50)
    @Column(length = 50, nullable = true)
    private String telefono;

    @Size(max = 100)
    @Column(length = 100, nullable = true)
    private String correo;

    @Size(max = 50)
    @Column(length = 50, nullable = true)
    private String numeroCuenta;

}
