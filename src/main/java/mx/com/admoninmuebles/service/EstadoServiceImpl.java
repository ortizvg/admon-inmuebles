package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.EstadoDto;
import mx.com.admoninmuebles.persistence.repository.EstadoRepository;

@Service
public class EstadoServiceImpl implements EstadoService{
	
	@Autowired
	private EstadoRepository estadoRepository;
	
    @Autowired
    private ModelMapper modelMapper;

	@Override
	public Collection<EstadoDto> buscarTodo() {
		 return StreamSupport.stream(estadoRepository.findAll().spliterator(), false).map(estado -> modelMapper.map(estado, EstadoDto.class)).collect(Collectors.toList());
	}

}
