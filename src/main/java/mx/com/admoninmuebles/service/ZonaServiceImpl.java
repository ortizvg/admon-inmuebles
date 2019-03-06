package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.NotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.dto.ZonaDto;
import mx.com.admoninmuebles.persistence.model.Asentamiento;
import mx.com.admoninmuebles.persistence.model.Zona;
import mx.com.admoninmuebles.persistence.repository.ZonaRepository;

@Service
public class ZonaServiceImpl implements ZonaService {

    @Autowired
    private ZonaRepository zonaRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private ColoniaService coloniaService;

    @Override
    public Zona save(final ZonaDto zonaDto) {
        return zonaRepository.save(modelMapper.map(zonaDto, Zona.class));
    }

    @Override
    public Collection<ZonaDto> findAll() {
        return StreamSupport.stream(zonaRepository.findAll().spliterator(), false).map(zona -> modelMapper.map(zona, ZonaDto.class)).collect(Collectors.toList());
    }

    @Override
    public ZonaDto findById(final String codigo) {
        Optional<Zona> zona = zonaRepository.findById(codigo);
        if( zona.isPresent() ) {
        	return modelMapper.map(zona.get(), ZonaDto.class);
        }
        
        throw new NotFoundException();
        
    }

    @Transactional
    @Override
    public void deleteById(final String codigo) {
    	Optional<Zona> zonaOpt = zonaRepository.findById(codigo);
        if( !zonaOpt.isPresent() ) {
        	throw new EntityNotFoundException();
        }
        
        Zona zona = zonaOpt.get();
        deleteAsentamientos( zona.getAsentamientos() );
        
        zonaRepository.deleteById(codigo);

    }

    @Override
    public boolean exist(final String codigo) {
        return zonaRepository.existsById(codigo);
    }

	@Override
	public Collection<ZonaDto> findByAdminZonaId(Long id) {
		
		return StreamSupport.stream(zonaRepository.findByAdminZonaId(id).spliterator(), false).map(zona -> modelMapper.map(zona, ZonaDto.class)).collect(Collectors.toList());
	}

	@Override
	public Collection<ZonaDto> findByAdministradoresBiId(Long id) {
		return StreamSupport.stream(zonaRepository.findByAdministradoresBiId(id).spliterator(), false).map(zona -> modelMapper.map(zona, ZonaDto.class)).collect(Collectors.toList());
	}

	@Override
	public ZonaDto findByProveedorId(Long id) {
		  Optional<Zona> zona = zonaRepository.findByProveedorId(id);
	        if( zona.isPresent() ) {
	        	return modelMapper.map(zona.get(), ZonaDto.class);
	        }
	        
	        throw new NotFoundException();
	}

	@Override
	public ZonaDto findByAdministradorBiId(Long id) {
		 Optional<Zona> zona = zonaRepository.findByProveedorId(id);
	        if( zona.isPresent() ) {
	        	return modelMapper.map(zona.get(), ZonaDto.class);
	        }
	        
	        return null;
//	        throw new NotFoundException();
	}
	
	private void deleteAsentamientos(Collection<Asentamiento> asentamientos) {
		if( !asentamientos.isEmpty() ) {
			
			asentamientos.stream().forEach( asentamiento -> {
		        	coloniaService.deleteById( asentamiento.getId() );
		     });
		}
	}

	@Override
	public void deleteByAdminZonaId(Long id) {
		Collection<Zona> zonas = zonaRepository.findByAdminZonaId(id);
		if( !zonas.isEmpty() ) {
			zonas.forEach( zona -> deleteById( zona.getCodigo() ));
		}
		
	}

}
