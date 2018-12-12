package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.SucursalDto;
import mx.com.admoninmuebles.persistence.model.Inmueble;
import mx.com.admoninmuebles.persistence.model.Sucursal;
import mx.com.admoninmuebles.persistence.repository.DireccionRepository;
import mx.com.admoninmuebles.persistence.repository.SucursalRepository;

@Service
public class SucursalServiceImpl implements SucursalService {

    @Autowired
    private SucursalRepository sucursalRepository;
    
    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SucursalDto save(final SucursalDto sucursalDto) {
    	Sucursal sucursal = modelMapper.map(sucursalDto, Sucursal.class);
    	sucursal.setDireccion(direccionRepository.save(sucursal.getDireccion()));
        Sucursal sucursalCreada =  sucursalRepository.save(modelMapper.map(sucursalDto, Sucursal.class));
        
        return modelMapper.map(sucursalCreada, SucursalDto.class);
    }

	@Override
	public Collection<SucursalDto> findAll() {
		return StreamSupport.stream(sucursalRepository.findAll().spliterator(), false)
				.map(sucursal -> modelMapper.map(sucursal, SucursalDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public SucursalDto findById(Long idSucursal) {
		Optional<Sucursal> sucursal = sucursalRepository.findById(idSucursal);
		return modelMapper.map(sucursal.get(), SucursalDto.class);
	}

	@Override
	public void deleteById(Long idSucursal) {
		sucursalRepository.deleteById(idSucursal);
		
	}

}
