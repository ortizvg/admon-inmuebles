package mx.com.admoninmuebles.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SocioCargaMasivaDto {

	private Long id;

	@NotNull
	@Size(min = 6, max = 25)
	private String username;

	@NotNull
	@Size(min = 1, max = 100)
	private String nombre;

	@NotNull
	@Size(max = 100)
	private String apellidoPaterno;

	private String apellidoMaterno;

	@NotNull
	@Email
	@Size(max = 100)
	private String correo;

	@Email
	@Size(max = 100)
	private String correoAlternativo1;

	@Email
	@Size(max = 100)
	private String correoAlternativo2;

	@NotNull
	@Digits(integer = 7, fraction = 2)
	private BigDecimal coutaMensualPagoSocio;

	private String datosDomicilio;

	@NotNull
	private Long rolSeleccionado;

	@NotNull
	private Long inmuebleId;

	@NotNull
	private Long tipoSocioId;

}
