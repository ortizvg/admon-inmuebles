package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.TipoReporteMensualDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.TipoReporteMensual;
import mx.com.admoninmuebles.persistence.repository.TipoReporteMensualRepository;

@Service
public class TipoReporteMensualServiceImpl implements TipoReporteMensualService{
	
	@Autowired
	private TipoReporteMensualRepository tipoReporteMensualRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Collection<TipoReporteMensualDto> buscarTodos() {
//		return StreamSupport.stream(tipoReporteMensualRepository.findAll().spliterator(), false)
//				.map(tipoReporteMensual -> modelMapper.map(tipoReporteMensual, TipoReporteMensualDto.class))
//				.collect(Collectors.toList());
		
		return StreamSupport.stream(tipoReporteMensualRepository.findByActivo( Boolean.TRUE ).spliterator(), false)
				.map(tipoReporteMensual -> modelMapper.map(tipoReporteMensual, TipoReporteMensualDto.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<TipoReporteMensualDto> buscarPorLang(String lang) {
//		return StreamSupport.stream(tipoReporteMensualRepository.findByLang( lang ).spliterator(), false)
//				.map(tipoReporteMensual -> modelMapper.map(tipoReporteMensual, TipoReporteMensualDto.class))
//				.collect(Collectors.toList());
		
		return StreamSupport.stream(tipoReporteMensualRepository.findByLangAndActivo( lang, Boolean.TRUE ).spliterator(), false)
				.map(tipoReporteMensual -> modelMapper.map(tipoReporteMensual, TipoReporteMensualDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public TipoReporteMensualDto buscarPorId(Long id) {
		Optional<TipoReporteMensual> tipoReporteMensualOpt = tipoReporteMensualRepository.findById( id );
		if( !tipoReporteMensualOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		return modelMapper.map(tipoReporteMensualOpt.get(), TipoReporteMensualDto.class);
	}

}
