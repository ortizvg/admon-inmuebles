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
	EstadoCuentaDto buscarRecientePorSocioId( Long idsocio );

}
