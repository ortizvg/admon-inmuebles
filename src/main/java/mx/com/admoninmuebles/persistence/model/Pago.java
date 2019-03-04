package mx.com.admoninmuebles.persistence.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "pagos")
@Data
@EqualsAndHashCode(callSuper = false)
public class Pago extends EntidadBase {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;
    
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id_pago")
//    private UUID id;

    @Size(min = 0, max = 50)
    @Column(length = 50, unique = true)
    private String numeroTransaccion;
    
    @Size(min = 0, max = 50)
    @Column(length = 50, unique = false)
    private String referencia;
    
    @Size(min = 0, max = 50)
    @Column(length = 50, unique = false)
    private String numeroCuenta;

    @Digits(integer = 7, fraction = 2)
    @Column(precision = 7, scale = 2)
    private BigDecimal monto;

    @Size(min = 0, max = 255)
    @Column(length = 255)
    private String concepto;

//    @Size(min = 0, max = 100)
//    @Column(length = 100, nullable=true)
//    private String comprobantePagoUrl;
    
//    @Lob
//    @Basic(fetch = FetchType.LAZY)
//    private byte[] comprobantePago;

    @NotNull
    private Boolean verificado;
    
    @Column(nullable = true)
    private LocalDate fechaPago;
    
    //NUEVO
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_verificacion")
    private Date fechaVerificacion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_pago_bancario", referencedColumnName = "id_tipo_pago_bancario", nullable = true)
    private TipoPagoBancario tipoPagoBancario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_pago", referencedColumnName = "id_tipo_pago", nullable = true)
    private TipoPago tipoPago;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estatus_pago", referencedColumnName = "id_estatus_pago", nullable = false)
    private EstatusPago estatusPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;
    
    //NUEVO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_generador", referencedColumnName = "id_usuario", nullable = true)
    private Usuario usuarioGenerador;
    
    //NUEVO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_verificador", referencedColumnName = "id_usuario", nullable = true)
    private Usuario usuarioVerificador;
    
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "id_archivo", referencedColumnName = "id_archivo", nullable = true)
    private Archivo comprobantePago;
    
}
