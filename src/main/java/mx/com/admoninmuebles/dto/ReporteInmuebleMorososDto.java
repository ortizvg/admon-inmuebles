package mx.com.admoninmuebles.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

import lombok.Data;

@Data
public class ReporteInmuebleMorososDto {
	
	private Long inmuebleId;
	private String inmubleNombre;
	private Long adminBiId;
	private String adminBiNombre;
	private String adminBiApellidoPaterno;
	private String adminBiApellidoMaterno;
	private Long pagosRealizados;
	private Long pagosVerificacion;
	private Long pagosPendientes;
	private Long pagosAtrasados;
	private Long pagosTotal;
	
	@Digits(integer = 7, fraction = 2)
	private BigDecimal pagosRealizadosPorcentaje;
	
	@Digits(integer = 7, fraction = 2)
	private BigDecimal pagosVerificacionPorcentaje;
	
	@Digits(integer = 7, fraction = 2)
	private BigDecimal pagosPendientesPorcentaje;
	
	@Digits(integer = 7, fraction = 2)
	private BigDecimal pagosAtrasadosPorcentaje;

}
