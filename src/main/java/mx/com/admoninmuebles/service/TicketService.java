package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.TicketDto;
import mx.com.admoninmuebles.persistence.model.Ticket;

public interface TicketService {
    Ticket save(TicketDto ticketDto);

    Collection<TicketDto> findAll();

    Collection<TicketDto> findByUsuarioCreadorId(Long id);

    Collection<TicketDto> findByUsuarioAsignadoId(Long id);

    TicketDto findById(Long id);

    void delete(Long id);
}
