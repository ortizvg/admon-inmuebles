package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.MesDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Mes;
import mx.com.admoninmuebles.persistence.model.MesRepository;

@Service
public class MesServiceImpl implements MesService{
	
	@Autowired
	private MesRepository mesRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Collection<MesDto> buscarTodos() {
		return StreamSupport.stream(mesRepository.findAll().spliterator(), false)
				.map(mes -> modelMapper.map(mes, MesDto.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<MesDto> buscarPorLang(String lang) {
		return StreamSupport.stream(mesRepository.findByLang(lang).spliterator(), false)
				.map(mes -> modelMapper.map(mes, MesDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public MesDto buscarPorId(Long id) {
		Optional<Mes> mesOpt = mesRepository.findById( id );
		if( !mesOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		return modelMapper.map(mesOpt.get(), MesDto.class);
	}

}
