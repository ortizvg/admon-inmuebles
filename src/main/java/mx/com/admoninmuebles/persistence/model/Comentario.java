package mx.com.admoninmuebles.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "comentarios")
@Data
@EqualsAndHashCode(callSuper = false)
public class Comentario extends EntidadBase {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Long id;

    @NotNull
    @Size(min = 1, max = 2000)
    @Column(length = 2000, nullable = false, columnDefinition = "text")
    private String comentario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;
    
    public Comentario() {
    	super();
    }
    
    
    public Comentario(String comentario, Usuario usuario) {
    	this.comentario = comentario;
    	this.usuario = usuario;
    }

}
