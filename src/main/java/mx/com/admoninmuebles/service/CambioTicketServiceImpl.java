package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public CambioTicketDto save(final CambioTicketDto cambioTicketDto) {
    	modelMapper.getConfiguration().setAmbiguityIgnored(true);
    	CambioTicket cambioTicket =  cambioTicketRepository.save(modelMapper.map(cambioTicketDto, CambioTicket.class));
        
        return modelMapper.map(cambioTicket, CambioTicketDto.class);
    }

	@Override
	public Collection<CambioTicketDto> findByTicketId(final Long id) {
		return StreamSupport.stream(cambioTicketRepository.findByTicketId(id).spliterator(), false).map(cambioTicket -> modelMapper.map(cambioTicket, CambioTicketDto.class)).collect(Collectors.toList());

	}

	@Override
	public CambioTicketDto findById(long id) {
        Optional<CambioTicket> cambioTicket = cambioTicketRepository.findById(id);
        return modelMapper.map(cambioTicket.get(), CambioTicketDto.class);
	}

}
