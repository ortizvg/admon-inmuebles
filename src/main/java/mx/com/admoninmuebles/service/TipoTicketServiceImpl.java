package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.TipoTicketDto;
import mx.com.admoninmuebles.persistence.model.TipoTicket;
import mx.com.admoninmuebles.persistence.repository.TipoTicketRepository;

@Service
public class TipoTicketServiceImpl implements TipoTicketService {

    @Autowired
    private TipoTicketRepository tipoTicketRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TipoTicket save(final TipoTicketDto tipoTicketDto) {
        return tipoTicketRepository.save(modelMapper.map(tipoTicketDto, TipoTicket.class));
    }

	@Override
	public Collection<TipoTicketDto> findAll() {
		return StreamSupport.stream(tipoTicketRepository.findAll().spliterator(), false)
				.map(tipoTicket -> modelMapper.map(tipoTicket, TipoTicketDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public TipoTicketDto findById(Long idTipoTicket) {
		Optional<TipoTicket> tipoTicket = tipoTicketRepository.findById(idTipoTicket);
		return modelMapper.map(tipoTicket.get(), TipoTicketDto.class);
	}

	@Override
	public void deleteById(Long idTipoTicket) {
		tipoTicketRepository.deleteById(idTipoTicket);
		
	}

}
