package mx.com.admoninmuebles.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.DireccionDto;
import mx.com.admoninmuebles.persistence.model.Direccion;
import mx.com.admoninmuebles.persistence.repository.DireccionRepository;

@Service
public class DireccionServiceImpl implements DireccionService {

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Direccion save(final DireccionDto direccionDto) {
        return direccionRepository.save(modelMapper.map(direccionDto, Direccion.class));
    }

}
