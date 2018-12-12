package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.CambioTicketDto;
import mx.com.admoninmuebles.persistence.model.CambioTicket;

public interface CambioTicketService {
    CambioTicket save(CambioTicketDto cambioTicketDto);
}
