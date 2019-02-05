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
@Table(name = "tipos_pagos_bancarios")
@Data
@EqualsAndHashCode(callSuper = false)
public class TipoPagoBancario extends EntidadBase {

    private static final long serialVersionUID = 1L;
    
    @Transient
    public static final String TRANSFERENCIA = "TRANSFERENCIA";
    
    @Transient
    public static final String PAYPAL = "PAYPAL";
    
    @Transient
    public static final String TARJETA = "TARJETA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_pago_bancario")
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
