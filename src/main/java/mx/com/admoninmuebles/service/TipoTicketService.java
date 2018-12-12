package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.TipoTicketDto;
import mx.com.admoninmuebles.persistence.model.TipoTicket;

public interface TipoTicketService {
    TipoTicket save(TipoTicketDto tipoTicketDto);
    Collection<TipoTicketDto> findAll();
    TipoTicketDto findById(Long idTipoTicket);
    void deleteById(Long idTipoTicket);
}
