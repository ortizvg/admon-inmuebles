package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tipos_socios")
@Data
@EqualsAndHashCode(callSuper = false)
public class TipoSocio extends EntidadBase {
	
	private static final long serialVersionUID = 1L;
    
    @Transient
    public static final String CONDOMINO = "CONDOMINO";
    
    @Transient
    public static final String RESIDENTE = "RESIDENTE";

    @Id
    @Column(name = "id_tipo_socio")
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(length = 255, unique = false, nullable = false)
    private String name;
    
    @NotNull
    @Size(min = 1, max = 255)
    @Column(length = 255, unique = true, nullable = false)
    private String descripction;
    
    @NotNull
    @Size(min = 1, max = 2)
    @Column(length = 2, unique = false, nullable = false)
    private String lang;

}
