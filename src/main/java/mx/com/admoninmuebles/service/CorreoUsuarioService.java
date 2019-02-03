package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.UsuarioDto;

public interface CorreoUsuarioService {
	
	void enviarActivacion(final UsuarioDto usuarioDto, final  String urlContext);
	void enviarRecuperacionContrasenia(final UsuarioDto usuarioDto, final  String urlContext);

}
