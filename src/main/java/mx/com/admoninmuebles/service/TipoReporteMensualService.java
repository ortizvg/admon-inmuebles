package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.TipoReporteMensualDto;

public interface TipoReporteMensualService {
	
	Collection<TipoReporteMensualDto> buscarTodos();
	TipoReporteMensualDto buscarPorId( Long id );

}
