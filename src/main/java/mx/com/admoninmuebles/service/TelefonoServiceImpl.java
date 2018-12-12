package mx.com.admoninmuebles.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.TelefonoDto;
import mx.com.admoninmuebles.persistence.model.Telefono;
import mx.com.admoninmuebles.persistence.repository.TelefonoRepository;

@Service
public class TelefonoServiceImpl implements TelefonoService {

    @Autowired
    private TelefonoRepository telefonoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Telefono save(final TelefonoDto telefonoDto) {
        return telefonoRepository.save(modelMapper.map(telefonoDto, Telefono.class));
    }

}
