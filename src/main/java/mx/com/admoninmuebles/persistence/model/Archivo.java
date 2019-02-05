package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Table(name = "archivos")
@Data
@EqualsAndHashCode(callSuper = false)
public class Archivo extends EntidadBase {

	private static final long serialVersionUID = 1L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.)
//	private UUID id;
	
	@Id
	@Size(min = 1, max = 200)
	@Column(name = "id_archivo", length = 200)
	private String id;
	
	@NotNull
    @Size(min = 1, max = 255)
    @Column(length = 255, unique = false, nullable=false)
	private String nombre;
	
    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = false, nullable=false)
	private String tipoContenido;
	
    @NotNull
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] bytes;

}
