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
@Table(name = "preguntas_frecuentes")
@Data
@EqualsAndHashCode(callSuper = false)
public class PreguntaFrecuente extends EntidadBase {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta_frecuente")
    private Long id;

    @NotNull
    @Size(min = 1, max = 500)
    @Column(length = 500, columnDefinition = "text", nullable = false)
    private String pregunta;

    @NotNull
    @Size(min = 1, max = 2000)
    @Column(length = 2000, columnDefinition = "text", nullable = false)
    private String respuesta;
    
    @NotNull
    @Size(min = 1, max = 3)
    @Column(length = 100, nullable = false)
    private String idioma;

}
