package mx.com.admoninmuebles.persistence.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The persistent class for the privileges database table.
 *
 */
@Entity
@Table(name = "privilegios")
@NamedQuery(name = "Privilegio.findAll", query = "SELECT p FROM Privilegio p")
@Data
@EqualsAndHashCode(callSuper = false)
public class Privilegio extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_privilegio", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    private String descripcion;

    @ManyToMany(mappedBy = "privilegios")
    private Collection<Rol> roles;

    private boolean activo;

}