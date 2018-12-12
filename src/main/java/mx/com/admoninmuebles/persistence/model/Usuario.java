package mx.com.admoninmuebles.persistence.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "usuarios")
@NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
@Data
@EqualsAndHashCode(callSuper = false)
public class Usuario extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_usuario", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 6, max = 25)
    @Column(length = 25)
    private String username;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100)
    private String nombre;

    @Size(max = 100)
    @Column(length = 100)
    private String apellidoPaterno;

    @Size(max = 100)
    private String apellidoMaterno;

    @NotNull
    @Email
    @Size(max = 100)
    @Column(length = 100)
    private String correo;

    @Size(max = 15)
    @Column(length = 15)
    private String telefonoFijo;

    @Size(max = 15)
    @Column(length = 15)
    private String telefonoOficina;

    @Size(max = 15)
    @Column(length = 15)
    private String telefonoMovil;

    @Size(max = 15)
    @Column(length = 15)
    private String telefonoAlternativo;

    @Size(max = 100)
    @Column(length = 100)
    private String facebook;

    @Size(max = 100)
    @Column(length = 100)
    private String twiter;

    @Size(max = 100)
    @Column(length = 100)
    private String youtube;

    @Size(max = 1000)
    @Column(length = 1000, columnDefinition = "text", nullable = true)
    private String googleMapsDir;

    @Size(max = 256)
    @Column(length = 256)
    private String fotoUrl;

    @Column(name = "cuenta_expirada")
    private boolean cuentaExpirada;

    @Column(name = "cuenta_bloqueada")
    private boolean cuentaBloqueada;

    @Column(name = "credenciales_expiradas")
    private boolean credencialesExpiradas;

    private boolean activo = true;

    private String identificador;

    private String contrasenia;

    @Size(max = 256)
    @Column(length = 256)
    private String datosDomicilio;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Collection<Rol> roles;

    @OneToOne
    @JoinColumn(name = "id_datos_adicionales_fk", nullable = true)
    private DatosAdicionales datosAdicionales;

    @OneToOne
    @JoinColumn(name = "id_direccion_fk", nullable = true)
    private Direccion direccion;

    @OneToOne(cascade = CascadeType.MERGE, mappedBy = "usuario")
    private RecuperacionContraseniaToken recuperacionContraseniaToken;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usuario")
    private ActivacionUsuarioToken activacionUsuarioToken;

    // @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    // private Collection<Telefono> telefonos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Collection<Comentario> comentarios;

    @ManyToMany
    @JoinTable(name = "usuarios_areas_servicios", joinColumns = @JoinColumn(name = "id_proveedor_fk", referencedColumnName = "id_usuario"),
               inverseJoinColumns = @JoinColumn(name = "id_area_servicio_fk", referencedColumnName = "id_area_servicio"))
    private Collection<AreaServicio> areasServicio = new HashSet<>();

    public void addAreaServicio(final AreaServicio areaServicio) {
        areasServicio.add(areaServicio);
        areaServicio.getProveedores().add(this);
    }

    public void removeAreaServicio(final AreaServicio areaServicio) {
        areasServicio.remove(areaServicio);
        areaServicio.getProveedores().remove(this);
    }

}