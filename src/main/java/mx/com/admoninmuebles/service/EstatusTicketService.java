package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.EstatusTicketDto;
import mx.com.admoninmuebles.persistence.model.EstatusTicket;

public interface EstatusTicketService {
    EstatusTicket save(EstatusTicketDto estatusTicketDto);
    Collection<EstatusTicketDto> findAll();
    EstatusTicketDto findById(Long idEstatusTicket);
    void deleteById(Long idEstatusTicket);
}
