package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.TipoPagoBancarioDto;
import mx.com.admoninmuebles.persistence.model.TipoPagoBancario;
import mx.com.admoninmuebles.persistence.repository.TipoPagoBancarioRepository;

@Service
public class TipoPagoBancarioServiceImpl implements TipoPagoBancarioService {

    @Autowired
    private TipoPagoBancarioRepository tipoPagoBancarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TipoPagoBancario save(final TipoPagoBancarioDto tipoPagoBancarioDto) {
        return tipoPagoBancarioRepository.save(modelMapper.map(tipoPagoBancarioDto, TipoPagoBancario.class));
    }

	@Override
	public Collection<TipoPagoBancarioDto> findAll() {
		return StreamSupport.stream(tipoPagoBancarioRepository.findAll().spliterator(), false)
				.map(tipoPagoBancario -> modelMapper.map(tipoPagoBancario, TipoPagoBancarioDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public TipoPagoBancarioDto findById(Long idTipoPagoBancario) {
		Optional<TipoPagoBancario> tipoPagoBancario = tipoPagoBancarioRepository.findById(idTipoPagoBancario);
		return modelMapper.map(tipoPagoBancario.get(), TipoPagoBancarioDto.class);
	}

	@Override
	public void deleteById(Long idTipoPagoBancario) {
		tipoPagoBancarioRepository.deleteById(idTipoPagoBancario);
		
	}

}
