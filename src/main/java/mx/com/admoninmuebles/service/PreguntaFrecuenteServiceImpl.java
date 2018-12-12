package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.PreguntaFrecuenteDto;
import mx.com.admoninmuebles.persistence.model.PreguntaFrecuente;
import mx.com.admoninmuebles.persistence.repository.PreguntaFrecuenteRepository;

@Service
public class PreguntaFrecuenteServiceImpl implements PreguntaFrecuenteService {

    @Autowired
    private PreguntaFrecuenteRepository preguntaFrecuenteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PreguntaFrecuente save(final PreguntaFrecuenteDto preguntaFrecuenteDto) {
        return preguntaFrecuenteRepository.save(modelMapper.map(preguntaFrecuenteDto, PreguntaFrecuente.class));
    }

	@Override
	public Collection<PreguntaFrecuenteDto> findAll() {
		return StreamSupport.stream(preguntaFrecuenteRepository.findAll().spliterator(), false)
				.map(preguntaFrecuente -> modelMapper.map(preguntaFrecuente, PreguntaFrecuenteDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public PreguntaFrecuenteDto findById(Long idPreguntaFrecuente) {
		Optional<PreguntaFrecuente> preguntaFrecuente = preguntaFrecuenteRepository.findById(idPreguntaFrecuente);
		return modelMapper.map(preguntaFrecuente.get(), PreguntaFrecuenteDto.class);
	}

	@Override
	public void deleteById(Long idPreguntaFrecuente) {
		preguntaFrecuenteRepository.deleteById(idPreguntaFrecuente);
		
	}

	@Override
	public Collection<PreguntaFrecuenteDto> findByIdioma(String idioma) {
		return StreamSupport.stream(preguntaFrecuenteRepository.findByIdioma( idioma ).spliterator(), false)
				.map(preguntaFrecuente -> modelMapper.map(preguntaFrecuente, PreguntaFrecuenteDto.class))
				.collect(Collectors.toList());
	}

}
