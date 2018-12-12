package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.AvisoOportunoDto;
import mx.com.admoninmuebles.persistence.model.AvisoOportuno;
import mx.com.admoninmuebles.persistence.repository.AvisoOportunoRepository;

@Service
public class AvisoOportunoServiceImpl implements AvisoOportunoService {

    @Autowired
    private AvisoOportunoRepository avisoOportunoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AvisoOportuno save(final AvisoOportunoDto avisoOportunoDto) {
        return avisoOportunoRepository.save(modelMapper.map(avisoOportunoDto, AvisoOportuno.class));
    }

	@Override
	public Collection<AvisoOportunoDto> findAll() {
		return StreamSupport.stream(avisoOportunoRepository.findAll().spliterator(), false)
				.map(avisoOportuno -> modelMapper.map(avisoOportuno, AvisoOportunoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public AvisoOportunoDto findById(Long idAvisoOportuno) {
		Optional<AvisoOportuno> avisoOportuno = avisoOportunoRepository.findById(idAvisoOportuno);
		return modelMapper.map(avisoOportuno.get(), AvisoOportunoDto.class);
	}

	@Override
	public void deleteById(Long idAvisoOportuno) {
		avisoOportunoRepository.deleteById(idAvisoOportuno);
		
	}

}
