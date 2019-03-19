package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.List;

import mx.com.admoninmuebles.dto.CambioTicketDto;
import mx.com.admoninmuebles.dto.TicketDto;
import mx.com.admoninmuebles.persistence.model.CambioTicket;

public interface CambioTicketService {
	CambioTicketDto save(CambioTicketDto cambioTicketDto);
	Collection<CambioTicketDto> findByTicketId(Long id);
	CambioTicketDto findById(long id);
}
