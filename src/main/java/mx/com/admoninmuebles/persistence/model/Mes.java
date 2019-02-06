package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "meses")
@Data
@EqualsAndHashCode(callSuper = false)
public class Mes {
	
	@Id
	@Column(name = "id_mes")
	@Min(1)
    @Max(12)
	private Long id;
	
	@NotNull
    @Size(min = 1, max = 255)
    @Column(length = 255, unique = true, nullable=false)
	private String descripcion;
	

}
