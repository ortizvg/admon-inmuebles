package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "avisos_oportunos")
@Data
@EqualsAndHashCode(callSuper = false)
public class AvisoOportuno extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aviso_oportuno")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    private String titulo;

    @NotNull
    @Size(min = 1, max = 2000)
    @Column(length = 2000, nullable = false, columnDefinition = "text")
    private String descripcion;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, unique = true, nullable = false)
    private String imagenUrl;

}
