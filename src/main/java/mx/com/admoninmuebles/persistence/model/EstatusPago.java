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
@Table(name = "estatus_pagos")
@Data
@EqualsAndHashCode(callSuper = false)
public class EstatusPago extends EntidadBase {
	
	 private static final long serialVersionUID = 1L;
	    
	    @Transient
	    public static final String PAGADO = "PAGADO";
	    
	    @Transient
	    public static final String CERCANO = "CERCANO";
	    
	    @Transient
	    public static final String VERIFICACION = "VERIFICACION";
	    
	    @Transient
	    public static final String ATRASADO = "ATRASADO";
	    

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_estatus_pago")
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
