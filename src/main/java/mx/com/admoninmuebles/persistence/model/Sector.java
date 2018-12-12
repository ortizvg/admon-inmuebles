package mx.com.admoninmuebles.persistence.model;

import java.io.Serializable;

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
@Table(name = "sectores")
@Data
@EqualsAndHashCode(callSuper = false)
public class Sector implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_sector")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, unique = true, nullable = false)
    private String nombre;
    
    @NotNull
    @Size(min = 1, max = 3)
    @Column(length = 100, nullable = false)
    private String idioma;

}
