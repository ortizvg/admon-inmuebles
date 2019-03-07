package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.TipoSocioDto;
import mx.com.admoninmuebles.persistence.model.TipoSocio;
import mx.com.admoninmuebles.persistence.repository.TipoSocioRepository;

@Service
public class TipoSocioServiceImpl implements TipoSocioService{
	
	@Autowired
	private TipoSocioRepository tipoSocioRepository;
	
    @Autowired
    private ModelMapper modelMapper;

	@Override
	public Collection<TipoSocioDto> findAll() {
//		return StreamSupport.stream(tipoSocioRepository.findAll().spliterator(), false)
//				.map(tipoSocio -> modelMapper.map(tipoSocio, TipoSocioDto.class))
//				.collect(Collectors.toList());
		
		return StreamSupport.stream(tipoSocioRepository.findByActivo( Boolean.TRUE ).spliterator(), false)
				.map(tipoSocio -> modelMapper.map(tipoSocio, TipoSocioDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<TipoSocioDto> findAllByLang(String lang) {
//		return StreamSupport.stream(tipoSocioRepository.findByLang(lang).spliterator(), false)
//				.map(tipoSocio -> modelMapper.map(tipoSocio, TipoSocioDto.class))
//				.collect(Collectors.toList());
		
		return StreamSupport.stream(tipoSocioRepository.findByLangAndActivo( lang, Boolean.TRUE ).spliterator(), false)
				.map(tipoSocio -> modelMapper.map(tipoSocio, TipoSocioDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public TipoSocioDto findById(Long idTipoSocio) {
		Optional<TipoSocio> tipoPago = tipoSocioRepository.findById( idTipoSocio );
		return modelMapper.map(tipoPago.get(), TipoSocioDto.class);
	}
	


}
