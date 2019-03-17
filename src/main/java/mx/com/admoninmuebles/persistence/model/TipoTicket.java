package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tipos_ticket")
@Data
@EqualsAndHashCode(callSuper = false)
public class TipoTicket extends EntidadBase {
    private static final long serialVersionUID = 1L;
    
    @Transient
    public static final Long ADMINISTRATIVO = 1L;
    
    @Transient
    public static final Long FINANCIERO = 2L;
    
    @Transient
    public static final Long OPERACION = 3L;
    
    @Transient
    public static final Long QUEJAS_SUGERENCIAS = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_ticket")
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String nombre;
}
