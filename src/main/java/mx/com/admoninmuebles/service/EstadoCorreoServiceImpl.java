package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.EstadoCorreoDto;
import mx.com.admoninmuebles.persistence.model.EstadoCorreo;
import mx.com.admoninmuebles.persistence.repository.EstadoCorreoRepository;

@Service
public class EstadoCorreoServiceImpl implements EstadoCorreoService{
	
    @Autowired
    private EstadoCorreoRepository estadoCorreoRepository;
    
    @Autowired
    private ModelMapper modelMapper;

	@Override
	public Collection<EstadoCorreoDto> findAll() {
        return StreamSupport.stream(estadoCorreoRepository.findAll().spliterator(), false).map(estadoCorreo -> modelMapper.map(estadoCorreo, EstadoCorreoDto.class)).collect(Collectors.toList());
	}

	@Override
	public EstadoCorreoDto findById(Long id) {
		Optional<EstadoCorreo> estadoCorreoOpt = estadoCorreoRepository.findById(id);
		 return modelMapper.map(estadoCorreoOpt.get(), EstadoCorreoDto.class);
	}

}
