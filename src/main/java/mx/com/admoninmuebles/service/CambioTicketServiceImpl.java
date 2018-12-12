package mx.com.admoninmuebles.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.CambioTicketDto;
import mx.com.admoninmuebles.persistence.model.CambioTicket;
import mx.com.admoninmuebles.persistence.repository.CambioTicketRepository;

@Service
public class CambioTicketServiceImpl implements CambioTicketService {

    @Autowired
    private CambioTicketRepository cambioTicketRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CambioTicket save(final CambioTicketDto cambioTicketDto) {
        return cambioTicketRepository.save(modelMapper.map(cambioTicketDto, CambioTicket.class));
    }

}
