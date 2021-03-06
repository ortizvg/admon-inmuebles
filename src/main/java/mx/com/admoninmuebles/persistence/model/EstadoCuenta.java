package mx.com.admoninmuebles.persistence.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Size(min = 0, max = 255)
	@Column(length = 255, unique = false, nullable = true)
	private String descripcion;

	@OneToOne( cascade = CascadeType.REMOVE )
	@JoinColumn(name = "id_archivo_fk", nullable = false)
	private Archivo archivo;

    @ManyToOne
    @JoinColumn(name = "id_socio_fk")
	private Usuario socio;

}
