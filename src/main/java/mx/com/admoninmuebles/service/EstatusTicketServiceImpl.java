package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.EstatusTicketDto;
import mx.com.admoninmuebles.persistence.model.EstatusTicket;
import mx.com.admoninmuebles.persistence.repository.EstatusTicketRepository;

@Service
public class EstatusTicketServiceImpl implements EstatusTicketService {

    @Autowired
    private EstatusTicketRepository estatusTicketRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public EstatusTicket save(final EstatusTicketDto estatusTicket) {
        return estatusTicketRepository.save(modelMapper.map(estatusTicket, EstatusTicket.class));
    }

	@Override
	public Collection<EstatusTicketDto> findAll() {
		return StreamSupport.stream(estatusTicketRepository.findAll().spliterator(), false)
		.map(estatusTicket -> modelMapper.map(estatusTicket, EstatusTicketDto.class))
		.collect(Collectors.toList());
	}

	@Override
	public EstatusTicketDto findById(Long idEstatusTicket) {
		Optional<EstatusTicket> estatusTicket = estatusTicketRepository.findById(idEstatusTicket);
		return modelMapper.map(estatusTicket.get(), EstatusTicketDto.class);
	}

	@Override
	public void deleteById(Long idEstatusTicket) {
		estatusTicketRepository.deleteById(idEstatusTicket);
		
	}

}
