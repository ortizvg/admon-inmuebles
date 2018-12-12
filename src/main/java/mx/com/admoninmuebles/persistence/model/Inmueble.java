package mx.com.admoninmuebles.persistence.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "inmuebles")
@Data
@EqualsAndHashCode(callSuper = false)
public class Inmueble extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inmueble")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    private String nombre;

    @NotNull
    @Min(value = 1)
    @Max(value = 31)
    @Column(name = "dia_cuota_ordinaria", nullable = false)
    private Integer diaCuotaOrdinaria;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    @Column(name = "monto_cuota_ordinaria", nullable = false, precision = 5, scale = 2)
    private BigDecimal montoCuotaOrdinaria;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "imagen_url", length = 100, nullable = false)
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "id_admin_bi_fk", nullable = false)
    public Usuario adminBi;

    @OneToOne
    @JoinColumn(name = "id_direccion_fk", nullable = false)
    private Direccion direccion;

    @OneToOne
    @JoinColumn(name = "id_datos_adicionales_fk", nullable = false)
    private DatosAdicionales datosAdicionales;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inmueble")
    private Collection<Notificacion> notificaciones;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Collection<Usuario> socios = new HashSet<>();

    public void addSocio(final Usuario socio) {
        socios.add(socio);
    }

    @OneToMany(mappedBy = "inmueble")
    private Collection<AreaComun> areasComunes = new ArrayList<>();

    public void addAreaComun(final AreaComun areaComun) {
        areasComunes.add(areaComun);
        areaComun.setInmueble(this);
    }

}
