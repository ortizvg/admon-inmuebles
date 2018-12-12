package mx.com.admoninmuebles.persistence.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "areas_servicio")
@Data
@EqualsAndHashCode(callSuper = false)
public class AreaServicio extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area_servicio")
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false)
    private String nombre;

    @ManyToMany(mappedBy = "areasServicio")
    private Collection<Usuario> proveedores = new HashSet<>();

    public void addProveedor(final Usuario proveedor) {
        proveedores.add(proveedor);
        proveedor.getAreasServicio().add(this);
    }

    public void removeProveedor(final Usuario proveedor) {
        proveedores.remove(proveedor);
        proveedor.getAreasServicio().remove(this);
    }

}
