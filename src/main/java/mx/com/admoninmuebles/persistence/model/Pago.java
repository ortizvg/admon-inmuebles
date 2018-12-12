package mx.com.admoninmuebles.persistence.model;

import java.math.BigDecimal;

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

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String numeroTransaccion;

    @Digits(integer = 7, fraction = 2)
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal monto;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    private String concepto;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    private String comprobantePagoUrl;

    @NotNull
    private Boolean verificado;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_pago_bancario", referencedColumnName = "id_tipo_pago_bancario", nullable = false)
    private TipoPagoBancario tipoPagoBancario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_pago", referencedColumnName = "id_tipo_pago", nullable = false)
    private TipoPago tipoPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;

}
