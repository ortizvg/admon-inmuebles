package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.EstadoCuentaDto;

public interface EstadoCuentaService {
	
	EstadoCuentaDto guardar( EstadoCuentaDto estadoCuentaDto );
	EstadoCuentaDto actualizar( EstadoCuentaDto estadoCuentaDto );
	void eliminar( Long idEstadoCuenta );
	EstadoCuentaDto buscarPorId( Long idEstadoCuenta );
	Collection<EstadoCuentaDto> buscarPorInmuebleId( Long idInmueble );
	Collection<EstadoCuentaDto> buscarPorSocioId( Long idsocio );
	Collection<EstadoCuentaDto> buscarPorContadorId(Long idContador);
	EstadoCuentaDto buscarRecientePorSocioId( Long idsocio );
	Collection<EstadoCuentaDto> buscarPorAdminBiId(Long idAdminBi);
	Collection<EstadoCuentaDto> buscarPorAdminZonaId(Long idAdminZona);
	Collection<EstadoCuentaDto> buscarTodo();
	void guardarPorInmueble(EstadoCuentaDto estadoCuentaDto);
	

}
