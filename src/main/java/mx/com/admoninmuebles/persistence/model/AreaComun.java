package mx.com.admoninmuebles.persistence.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "areas_comunes")
@Data
@EqualsAndHashCode(callSuper = false)
public class AreaComun extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area_comun")
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false)
    @NaturalId
    private String nombre;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 300, nullable = false)
    @NaturalId
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_inmueble_fk")
    private Inmueble inmueble;

    @OneToMany(mappedBy = "areaComun")
    private List<Reservacion> reservaciones = new ArrayList<>();

    public void addReservacion(final Reservacion reservacion) {
        reservaciones.add(reservacion);
        reservacion.setAreaComun(this);
    }

}
