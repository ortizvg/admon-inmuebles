package mx.com.admoninmuebles.validation;

import java.util.List;

import mx.com.admoninmuebles.dto.ErrorDto;
import mx.com.admoninmuebles.dto.UsuarioDto;

public interface CargaMasivaSocioValidator {
	
	List<ErrorDto> validarCargaMasiva(List<UsuarioDto> listaSocios);

}
