package mx.com.admoninmuebles.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

import lombok.Data;

@Data
public class ReporteTicketDto {
	
	private Long inmuebleId;
	private String inmubleNombre;
	private String inmubleImagenUrl;
	private Long adminBiId;
	private String adminBiNombre;
	private String adminBiApellidoPaterno;
	private String adminBiApellidoMaterno;
	private Long ticketsAbiertos;
	private Long ticketsEnProceso;
	private Long ticketsCerrados;
	private Long ticketsTotal;
	
	@Digits(integer = 7, fraction = 2)
	private BigDecimal ticketsAbiertosPorcentaje;
	
	@Digits(integer = 7, fraction = 2)
	private BigDecimal ticketsEnProcesoPorcentaje;
	
	@Digits(integer = 7, fraction = 2)
	private BigDecimal ticketsCerradosPorcentaje;

}
