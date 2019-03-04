package mx.com.admoninmuebles.persistence.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "zonas")
@NamedQuery(name = "Zona.findAll", query = "SELECT z FROM Zona z")
@Data
@EqualsAndHashCode(callSuper = false)
public class Zona extends EntidadBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Size(min = 4, max = 10)
	@Column(length = 10)
	private String codigo;

	@NotNull
	@Size(min = 4, max = 100)
	@Column(length = 100)
	private String nombre;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "id_admin_zona_fk")
	private Usuario adminZona;
	
	@ManyToOne
	@JoinColumn(name = "id_estado_fk")
	private Estado estado;
	
	
	@OneToMany(mappedBy = "zona")
	private Collection<Asentamiento> asentamientos;

	@OneToMany
	private Collection<Usuario> administradoresBi = new ArrayList<>();
	
	@OneToMany(cascade = { CascadeType.MERGE })
	private Collection<Usuario> proveedores = new ArrayList<>();

	public void addAdminBi(Usuario adminBi) {
    	administradoresBi.add(adminBi);
    }

}
