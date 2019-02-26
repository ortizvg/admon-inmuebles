package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ContadorDto;
import mx.com.admoninmuebles.dto.UsuarioDto;

public interface ContadorService  {
	Collection<UsuarioDto> buscarTodos();
	ContadorDto buscarContadorPorId(Long idContador);
	ContadorDto guardar(ContadorDto contadorDto);
	void eliminar(Long idContador);
	boolean exist(final Long id);
}
