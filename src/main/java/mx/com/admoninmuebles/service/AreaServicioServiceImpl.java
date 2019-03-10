package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.controller.ReporteMensualController;
import mx.com.admoninmuebles.dto.AreaServicioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.AreaServicio;
import mx.com.admoninmuebles.persistence.repository.AreaServicioRepository;

@Service
public class AreaServicioServiceImpl implements AreaServicioService {
	
	Logger logger = LoggerFactory.getLogger(AreaServicioServiceImpl.class);

    @Autowired
    private AreaServicioRepository areaServicioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AreaServicioDto save(final AreaServicioDto areaServicioDto) {
    	
    	if( areaServicioRepository.existsByNombre( areaServicioDto.getNombre() )) {
    		throw new BusinessException("areaservicio.crear.mensaje.validacion.ya.existe");
    	}
    	
    	areaServicioDto.setActivo( Boolean.TRUE );
    	AreaServicio areaServicioCreada = null;
    	areaServicioCreada =  areaServicioRepository.save(modelMapper.map(areaServicioDto, AreaServicio.class));
        
        return modelMapper.map(areaServicioCreada, AreaServicioDto.class);
    }

    @Override
    public Collection<AreaServicioDto> findAll() {
//        return StreamSupport.stream(areaServicioRepository.findAll().spliterator(), false).map(areaServicio -> modelMapper.map(areaServicio, AreaServicioDto.class)).collect(Collectors.toList());
        return StreamSupport.stream(areaServicioRepository.findByActivo( Boolean.TRUE ).spliterator(), false).map(areaServicio -> modelMapper.map(areaServicio, AreaServicioDto.class)).collect(Collectors.toList());
    }

    @Override
    public AreaServicioDto findById(final Long id) {
        Optional<AreaServicio> areaServicio = areaServicioRepository.findById(id);
        return modelMapper.map(areaServicio.get(), AreaServicioDto.class);
    }

    @Override
    public void delete(final Long id) {
    	Optional<AreaServicio> areaServicioOpt = areaServicioRepository.findById(id);
    	if( areaServicioOpt.isPresent() ) {
    		AreaServicio areaServicio = areaServicioOpt.get();
    		areaServicio.setActivo( Boolean.FALSE );
    		areaServicioRepository.save( areaServicio );
//    		areaServicioRepository.deleteById(id);
    	}
    }

}
