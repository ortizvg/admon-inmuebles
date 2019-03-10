package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.TipoPagoDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.TipoPago;
import mx.com.admoninmuebles.persistence.repository.TipoPagoRepository;

@Service
public class TipoPagoServiceImpl implements TipoPagoService {

    @Autowired
    private TipoPagoRepository tipoPagoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TipoPago save(final TipoPagoDto tipoPagoDto) {
    	tipoPagoDto.setActivo( Boolean.TRUE );
        return tipoPagoRepository.save(modelMapper.map(tipoPagoDto, TipoPago.class));
    }

	@Override
	public Collection<TipoPagoDto> findAll() {
//		return StreamSupport.stream(tipoPagoRepository.findAll().spliterator(), false)
//				.map(tipoPago -> modelMapper.map(tipoPago, TipoPagoDto.class))
//				.collect(Collectors.toList());
		
		return StreamSupport.stream(tipoPagoRepository.findByActivo( Boolean.TRUE ).spliterator(), false)
				.map(tipoPago -> modelMapper.map(tipoPago, TipoPagoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public TipoPagoDto findById(Long idTipoPago) {
		Optional<TipoPago> tipoPago = tipoPagoRepository.findById(idTipoPago);
		return modelMapper.map(tipoPago.get(), TipoPagoDto.class);
	}

	@Override
	public void deleteById(Long idTipoPago) {
		Optional<TipoPago> tipoPagoOpt = tipoPagoRepository.findById(idTipoPago);
		if( !tipoPagoOpt.isPresent() ) {
			throw new BusinessException();
		}
//		tipoPagoRepository.deleteById(idTipoPago);
		
		TipoPago tipoPago = tipoPagoOpt.get();
		tipoPago.setActivo( Boolean.FALSE );
		tipoPagoRepository.save( tipoPago );
		
	}

	@Override
	public Collection<TipoPagoDto> findAllByLang(String lang) {
//		return StreamSupport.stream(tipoPagoRepository.findAllByLang(lang).spliterator(), false)
//				.map(tipoPago -> modelMapper.map(tipoPago, TipoPagoDto.class))
//				.collect(Collectors.toList());
		
		return StreamSupport.stream(tipoPagoRepository.findAllByLanggAndActivo( lang, Boolean.TRUE ).spliterator(), false)
				.map(tipoPago -> modelMapper.map(tipoPago, TipoPagoDto.class))
				.collect(Collectors.toList());
	}

}
