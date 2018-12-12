package mx.com.admoninmuebles.persistence.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The persistent class for the roles database table.
 *
 */
@Entity
@Table(name = "roles")
@NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r")
@Data
@EqualsAndHashCode(callSuper = false)
public class Rol extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_rol", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    private String descripcion;

    private boolean activo;

    @ManyToMany(mappedBy = "roles")
    private Collection<Usuario> usuarios;

    @ManyToMany
    @JoinTable(
            name = "roles_privilegios",
            joinColumns = @JoinColumn(name = "id_rol", referencedColumnName = "id_rol"),
            inverseJoinColumns = @JoinColumn(name = "id_privilegio", referencedColumnName = "id_privilegio"))
    private Collection<Privilegio> privilegios;

}