package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.AreaComunDto;
import mx.com.admoninmuebles.persistence.model.AreaComun;
import mx.com.admoninmuebles.persistence.repository.AreaComunRepository;

@Service
public class AreaComunServiceImpl implements AreaComunService {

	@Autowired
	private AreaComunRepository areaComunRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public AreaComun save(final AreaComunDto areaComunDto) {
		return areaComunRepository.save(modelMapper.map(areaComunDto, AreaComun.class));
	}

	@Override
	public Collection<AreaComunDto> findAll() {
		return StreamSupport.stream(areaComunRepository.findAll().spliterator(), false)
				.map(areaComun -> modelMapper.map(areaComun, AreaComunDto.class)).collect(Collectors.toList());
	}

	@Override
	public AreaComunDto findById(final Long id) {
		Optional<AreaComun> areaComun = areaComunRepository.findById(id);
		return modelMapper.map(areaComun.get(), AreaComunDto.class);
	}

	@Override
	public void delete(final Long id) {
		areaComunRepository.deleteById(id);
	}

}
