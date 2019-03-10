package mx.com.admoninmuebles.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.TicketDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.persistence.model.Inmueble;
import mx.com.admoninmuebles.persistence.model.Ticket;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.model.Zona;
import mx.com.admoninmuebles.persistence.repository.DatosAdicionalesRepository;
import mx.com.admoninmuebles.persistence.repository.DireccionRepository;
import mx.com.admoninmuebles.persistence.repository.InmuebleRepository;
import mx.com.admoninmuebles.persistence.repository.TicketRepository;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;
import mx.com.admoninmuebles.persistence.repository.ZonaRepository;

@Service
public class InmuebleServiceImpl implements InmuebleService {
	
    @Autowired
    private InmuebleRepository inmuebleRepository;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private DatosAdicionalesRepository datosAdicionalesRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ZonaRepository zonaRepository;

    @Override
    public Inmueble save(final InmuebleDto inmuebleDto) {
        Inmueble inmueble = modelMapper.map(inmuebleDto, Inmueble.class);
        inmueble.setDireccion(direccionRepository.save(inmueble.getDireccion()));
        inmueble.setDatosAdicionales(datosAdicionalesRepository.save(inmueble.getDatosAdicionales()));
//        Optional<Inmueble> inmuebleTmp = inmuebleRepository.findById(inmueble.getId());
//        if(inmuebleTmp.isPresent()) {
//        	inmueble.setSocios(inmuebleTmp.get().getSocios());
//        }
        return inmuebleRepository.save(inmueble);
    }
    
    @Override
    public Inmueble update(final InmuebleDto inmuebleDto) {
        Inmueble inmueble = modelMapper.map(inmuebleDto, Inmueble.class);
        inmueble.setDireccion(direccionRepository.save(inmueble.getDireccion()));
        inmueble.setDatosAdicionales(datosAdicionalesRepository.save(inmueble.getDatosAdicionales()));
        Optional<Inmueble> inmuebleTmp = inmuebleRepository.findById(inmueble.getId());
        if(inmuebleTmp.isPresent()) {
        	inmueble.setSocios(inmuebleTmp.get().getSocios());
        }
        return inmuebleRepository.save(inmueble);
    }

    @Override
    public Collection<InmuebleDto> findAll() {
        return StreamSupport.stream(inmuebleRepository.findAll().spliterator(), false)
        		.map(inmueble -> 
					{
						InmuebleDto inmuebleDto = modelMapper.map(inmueble, InmuebleDto.class);
						inmuebleDto.setTotalSocios(inmueble.getSocios() != null ? inmueble.getSocios().size() : 0);
						
						return inmuebleDto;
					}
        		)
        		.collect(Collectors.toList());
    }

    @Override
    public InmuebleDto findById(final Long id) {
        Optional<Inmueble> inmueble = inmuebleRepository.findById(id);
        return modelMapper.map(inmueble.get(), InmuebleDto.class);
    }

    @Override
    public void deleteById(final Long id) {
        inmuebleRepository.deleteById(id);

    }

    @Override
    public boolean exist(final Long id) {
        return inmuebleRepository.existsById(id);
    }

	@Override
	public Collection<InmuebleDto> findByAdminBiId(Long id) {
//		return StreamSupport.stream(inmuebleRepository.findByAdminBiId(id).spliterator(), false).map(inmueble -> modelMapper.map(inmueble, InmuebleDto.class)).collect(Collectors.toList());
		
		return StreamSupport.stream(inmuebleRepository.findByAdminBiId(id).spliterator(), false)
				.map(inmueble -> 
					{
						InmuebleDto inmuebleDto = modelMapper.map(inmueble, InmuebleDto.class);
						inmuebleDto.setTotalSocios(inmueble.getSocios() != null ? inmueble.getSocios().size() : 0);
						
						return inmuebleDto;
					}
				)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<InmuebleDto> findByDireccionAsentamientoId(Long id) {
		 return StreamSupport.stream(inmuebleRepository.findByDireccionAsentamientoId(id).spliterator(), false)
	        		.map(inmueble -> 
						{
							InmuebleDto inmuebleDto = modelMapper.map(inmueble, InmuebleDto.class);
							inmuebleDto.setTotalSocios(inmueble.getSocios() != null ? inmueble.getSocios().size() : 0);
							
							return inmuebleDto;
						}
	        		)
				 .collect(Collectors.toList());
	}

	@Override
	public Collection<UsuarioDto> findSociosByInmuebleId(Long id) {
		Inmueble inmueble = inmuebleRepository.findById(id).get();
		
		Collection<Usuario> socios =  inmueble.getSocios().stream()
				 .filter(socio -> ( RolConst.ROLE_SOCIO_BI.equals(socio.getRoles().stream().findFirst().get().getNombre() ) ) )
				 .collect(Collectors.toList());
				 
		return StreamSupport.stream(socios.spliterator(), false).map(socio -> modelMapper.map(socio, UsuarioDto.class)).collect(Collectors.toList());
	}
	
	@Override
	public Collection<UsuarioDto> findSociosActivosByInmuebleId(Long id) {
		Inmueble inmueble = inmuebleRepository.findById(id).get();
		
		Collection<Usuario> socios =  inmueble.getSocios().stream()
				 .filter(socio -> ( RolConst.ROLE_SOCIO_BI.equals(socio.getRoles().stream().findFirst().get().getNombre() ) && socio.isActivo() ) )
				 .collect(Collectors.toList());
				 
		return StreamSupport.stream(socios.spliterator(), false).map(socio -> modelMapper.map(socio, UsuarioDto.class)).collect(Collectors.toList());
	}

	@Override
	public Collection<TicketDto> findTicketsByInmuebleId(Long id) {
		Inmueble inmueble = inmuebleRepository.findById(id).get();
		Collection<Usuario> socios = inmueble.getSocios();
		Collection<Ticket> tickets = new ArrayList<>();
		socios.stream().forEach(socio -> {
			Collection<Ticket> ticketsPorSocio = ticketRepository.findByUsuarioCreadorId(socio.getId());
			if( ticketsPorSocio != null && !ticketsPorSocio.isEmpty()) {
				tickets.addAll(ticketsPorSocio.stream().collect(Collectors.toList()));
			}
			});
		
		return StreamSupport.stream(tickets.spliterator(), false).map(ticket -> modelMapper.map(ticket, TicketDto.class)).collect(Collectors.toList());
	}
	
    @Override
    public Collection<InmuebleDto> findBySociosId(final Long id) {
    	 return StreamSupport.stream(inmuebleRepository.findBySociosId(id).spliterator(), false).map(inmueble -> modelMapper.map(inmueble, InmuebleDto.class)).collect(Collectors.toList());
    }
    
    @Override
    public InmuebleDto findBySocioId(final Long id) {
    	 return  modelMapper.map( inmuebleRepository.findBySocioId( id ), InmuebleDto.class );
    }

    @Transactional
	@Override
	public void addSocio2Inmueble(final UsuarioDto usuarioDto, final Long inmuebleId) {
		Inmueble inmueble = inmuebleRepository.findById(inmuebleId).get();
		Usuario socio = usuarioRepository.findById(usuarioDto.getId()).get();
		inmueble.addSocio(socio);
		inmuebleRepository.save(inmueble);
	}

	@Override
	public Collection<InmuebleDto> findByZonaCodigo(String codigo) {
		return StreamSupport.stream(inmuebleRepository.findByDireccionAsentamientoZonaCodigo(codigo).spliterator(), false).map(inmueble -> modelMapper.map(inmueble, InmuebleDto.class)).collect(Collectors.toList());
	}

	@Override
	public Collection<InmuebleDto> findByContadorId(Long id) {
		return StreamSupport.stream(inmuebleRepository.findByContadorId(id).spliterator(), false)
				.map(inmueble -> modelMapper.map(inmueble, InmuebleDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<InmuebleDto> findByAdminZonaId(Long id) {
		Collection<Zona> zonas = zonaRepository.findByAdminZonaId( id );
		return StreamSupport.stream(inmuebleRepository.findByDireccionAsentamientoZonaIn( zonas ).spliterator(), false)
//				.map(inmueble -> modelMapper.map(inmueble, InmuebleDto.class))
				.map(inmueble -> 
				{
					InmuebleDto inmuebleDto = modelMapper.map(inmueble, InmuebleDto.class);
					inmuebleDto.setTotalSocios(inmueble.getSocios() != null ? inmueble.getSocios().size() : 0);
					
					return inmuebleDto;
				}
			)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteByAdminBiId(Long id) {
		Collection<Inmueble> inmuebles = inmuebleRepository.findByAdminBiId(id);
		if( !inmuebles.isEmpty() ) {
			inmuebles.forEach(inmueble -> deleteById( inmueble.getId() ));
		}
		
	}

}
