package mx.com.admoninmuebles.validation;

import java.util.List;

import mx.com.admoninmuebles.dto.UsuarioDto;

public interface CargaMasivaSocioValidator {
	
	void validarCargaMasiva(List<UsuarioDto> listaSocios);

}
