package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.RecuperaContraseniaDto;
import mx.com.admoninmuebles.dto.UsuarioDto;

public interface RecuperacionContraseniaService {
	
	UsuarioDto guardarNuevaContrasenia(RecuperaContraseniaDto recuperaContraseniaDto);
	
	void guardarToken(final UsuarioDto usuarioDto, final String token);
	
	boolean isTokenValido(final String token);
}
