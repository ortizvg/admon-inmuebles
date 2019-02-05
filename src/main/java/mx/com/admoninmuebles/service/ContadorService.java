package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.UsuarioDto;

public interface ContadorService  {
	Collection<UsuarioDto> buscarTodos();
}
