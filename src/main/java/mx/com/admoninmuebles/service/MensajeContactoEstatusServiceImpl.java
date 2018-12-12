package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.MensajeContactoEstatusDto;
import mx.com.admoninmuebles.persistence.model.MensajeContactoEstatus;
import mx.com.admoninmuebles.persistence.repository.MensajeContactoEstatusRepository;

@Service
public class MensajeContactoEstatusServiceImpl implements MensajeContactoEstatusService{
	
	@Autowired
	private MensajeContactoEstatusRepository mensajeContactoEstatusRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Collection<MensajeContactoEstatusDto> findAll() {
		return StreamSupport.stream(mensajeContactoEstatusRepository.findAll().spliterator(), false)
		.map(mensajeContactoEstatus -> modelMapper.map(mensajeContactoEstatus, MensajeContactoEstatusDto.class))
		.collect(Collectors.toList());
	}

	@Override
	public MensajeContactoEstatusDto findById(Long id) {
		Optional<MensajeContactoEstatus> mensajeContactoEstatus = mensajeContactoEstatusRepository.findById(id);
		return modelMapper.map(mensajeContactoEstatus.get(), MensajeContactoEstatusDto.class);
	}

	@Override
	public Collection<MensajeContactoEstatusDto> findByIdioma(String idioma) {
		return StreamSupport.stream(mensajeContactoEstatusRepository.findByIdioma( idioma ).spliterator(), false)
				.map(mensajeContactoEstatus -> modelMapper.map(mensajeContactoEstatus, MensajeContactoEstatusDto.class))
				.collect(Collectors.toList());
	}

}
