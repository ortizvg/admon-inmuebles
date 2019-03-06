package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tipos_reporte_mensual")
@Data
@EqualsAndHashCode(callSuper = false)
public class TipoReporteMensual extends EntidadBase{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_tipo_reporte_mensual")
	private Long id;
	
	@NotNull
    @Size(min = 1, max = 255)
    @Column(length = 255, unique = true, nullable=false)
	private String descripcion;
	
    @NotNull
    @Size(min = 1, max = 2)
    @Column(length = 2, unique = false, nullable = false)
    private String lang;
    
    @NotNull
    private boolean activo;

}
