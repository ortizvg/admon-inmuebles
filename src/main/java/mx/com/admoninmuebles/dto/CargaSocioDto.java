package mx.com.admoninmuebles.dto;

import java.util.List;

import lombok.Data;

@Data
public class CargaSocioDto {

	private List<UsuarioDto> listaSocios;
	private List<ErrorDto> listaErrores;

}
