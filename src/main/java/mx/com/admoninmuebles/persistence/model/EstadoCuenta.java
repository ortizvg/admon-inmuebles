package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "estados_cuenta")
@Data
@EqualsAndHashCode(callSuper = false)
public class EstadoCuenta  extends EntidadBase {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_estado_cuenta")
	private Long id;

	@NotNull
	@Size(min = 1, max = 255)
	@Column(length = 255, unique = true, nullable = false)
	private String descripcion;

	@OneToOne
	@JoinColumn(name = "id_archivo_fk", nullable = false)
	private Archivo archivo;

	@OneToOne
	@JoinColumn(name = "id_socio_fk", nullable = false)
	private Usuario socio;

}
