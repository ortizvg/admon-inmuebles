package mx.com.admoninmuebles.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PagoTransferenciaBancariaDto {
	
	private Long id;

	@NotNull
	@Size(min = 1, max = 50)
	private String numeroTransaccion;

	@Digits(integer = 7, fraction = 2)
	private BigDecimal monto;

	@NotNull
	@Size(min = 1, max = 100)
	private String concepto;

	@NotNull
	@Size(min = 1, max = 100)
	private String comprobantePagoUrl;

	@NotNull
	private Boolean verificado;
	
	@NotNull
    private String referencia;
    
	@NotNull
    private String numeroCuenta;
	
	@NotNull
	private Long usuarioId;
	
    private Long tipoPagoBancarioId;

    private Long tipoPagoId;

}
