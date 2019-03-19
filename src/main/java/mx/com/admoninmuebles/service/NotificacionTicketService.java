package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.CambioTicketDto;
import mx.com.admoninmuebles.dto.TicketDto;

public interface NotificacionTicketService {
	
	void notificarCreacionTicket(final TicketDto ticketDto);
	
	void notificarAsignacionTicket(final TicketDto ticketDto);
	
	void notificarCierreTicket(final TicketDto ticketDto);
	
	void notificarComentarioTicket(final CambioTicketDto cambioTicketDto);

}
