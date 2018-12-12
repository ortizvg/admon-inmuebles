package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ProveedorDto;
import mx.com.admoninmuebles.dto.SocioDto;
import mx.com.admoninmuebles.dto.UsuarioDto;

public interface SocioService {
	
	Collection<SocioDto> getSocios();
	SocioDto buscarSocioPorId(Long idSocio);
	SocioDto guardar(SocioDto socioDto);
	void eliminar(Long idSocio);
	boolean exist(final Long id);
	Collection<UsuarioDto> findSociosByZonaCodigo(String zonaCodigo);
	Collection<UsuarioDto> findSociosByAdminBiId(Long id);

}
