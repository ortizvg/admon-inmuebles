package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.TicketDto;
import mx.com.admoninmuebles.persistence.model.Ticket;
import mx.com.admoninmuebles.persistence.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TicketDto save(final TicketDto ticketDto) {
        Ticket ticket =  ticketRepository.save(modelMapper.map(ticketDto, Ticket.class));
        return modelMapper.map(ticket, TicketDto.class);
    }

    @Override
    public Collection<TicketDto> findAll() {
        return StreamSupport.stream(ticketRepository.findAll().spliterator(), false).map(ticket -> modelMapper.map(ticket, TicketDto.class)).collect(Collectors.toList());
    }

    @Override
    public Collection<TicketDto> findByUsuarioCreadorId(final Long id) {
        return StreamSupport.stream(ticketRepository.findByUsuarioCreadorId(id).spliterator(), false).map(ticket -> modelMapper.map(ticket, TicketDto.class)).collect(Collectors.toList());
    }

    @Override
    public Collection<TicketDto> findByUsuarioAsignadoId(final Long id) {
        return StreamSupport.stream(ticketRepository.findByUsuarioAsignadoId(id).spliterator(), false).map(ticket -> modelMapper.map(ticket, TicketDto.class)).collect(Collectors.toList());
    }

    @Override
    public TicketDto findById(final Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        return modelMapper.map(ticket.get(), TicketDto.class);
    }

    @Override
    public void delete(final Long id) {
        ticketRepository.deleteById(id);
    }

	@Override
	public boolean isFiltro(TicketDto ticketDto) {
		return StringUtils.isNotBlank( ticketDto.getEstatus()) || ticketDto.getInmuebleId() != null || ticketDto.getTipoTicketId() != null;
	}

	@Override
	public Collection<TicketDto> filtrar(TicketDto ticketDto) {
		return StreamSupport.stream(ticketRepository.findByInmuebleIdAndTipoTicketNameAndEstatusTicketName(ticketDto.getInmuebleId(), ticketDto.getTipoTicketId(), ticketDto.getEstatus()).spliterator(), false).map(ticket -> modelMapper.map(ticket, TicketDto.class)).collect(Collectors.toList());
	}
	
}
