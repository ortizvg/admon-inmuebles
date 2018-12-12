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
@Table(name = "idioma")
@Data
@EqualsAndHashCode(callSuper = false)
public class Idioma extends EntidadBase {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_idioma")
    private Integer id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String nombre;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, unique = true, nullable = false)
    private String idiomaUrl;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public String getIdiomaUrl() {
        return idiomaUrl;
    }

    public void setIdiomaUrl(final String idiomaUrl) {
        this.idiomaUrl = idiomaUrl;
    }

}
