package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.SectorDto;
import mx.com.admoninmuebles.persistence.model.Sector;
import mx.com.admoninmuebles.persistence.repository.SectorRepository;

@Service
public class SectorServiceImpl implements SectorService{
	
	@Autowired
	private SectorRepository sectorRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Collection<SectorDto> findAll() {
		return StreamSupport.stream(sectorRepository.findAll().spliterator(), false)
		.map(sector -> modelMapper.map(sector, SectorDto.class))
		.collect(Collectors.toList());
	}

	@Override
	public SectorDto findById(Long id) {
		Optional<Sector> sector = sectorRepository.findById(id);
		return modelMapper.map(sector.get(), SectorDto.class);
	}

	@Override
	public Collection<SectorDto> findByIdioma(String idioma) {
		return StreamSupport.stream(sectorRepository.findByIdioma( idioma ).spliterator(), false)
				.map(sector -> modelMapper.map(sector, SectorDto.class))
				.collect(Collectors.toList());
	}

}
