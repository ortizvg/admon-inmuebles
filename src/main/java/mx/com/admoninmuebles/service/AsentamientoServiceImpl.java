package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.ColoniaDto;
import mx.com.admoninmuebles.persistence.model.Zona;
import mx.com.admoninmuebles.persistence.repository.AsentamientoRepository;
import mx.com.admoninmuebles.persistence.repository.ZonaRepository;

@Service
public class AsentamientoServiceImpl implements AsentamientoService {

	@Autowired
	private AsentamientoRepository asentamientoRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ZonaRepository zonaRepository;

	@Override
	public Collection<ColoniaDto> findBycodigoPostalAndZonaCodigo(String codigoPostal, String zonaCodigo) {

		Optional<Zona> zonaOpt = zonaRepository.findById(zonaCodigo);
		
		if( !zonaOpt.isPresent() || codigoPostal == null || codigoPostal.isEmpty() ) {
			return Collections.emptyList();
		}

		return StreamSupport
				.stream(asentamientoRepository
				.findBycodigoPostalAndMunicipioEstadoId(codigoPostal, zonaOpt.get().getEstado().getId()).spliterator(),false)
				.map(asentamiento -> modelMapper.map(asentamiento, ColoniaDto.class)).collect(Collectors.toList());
	}

}
