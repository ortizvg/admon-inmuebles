package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.dto.ActivacionUsuarioDto;

public interface ActivacionUsuarioService {
	
	UsuarioDto activar(ActivacionUsuarioDto verificacionContraseniaDto);
	
	void guardarToken(final UsuarioDto usuarioDto, final String token);
	
	boolean isTokenValido(final String token);

}
