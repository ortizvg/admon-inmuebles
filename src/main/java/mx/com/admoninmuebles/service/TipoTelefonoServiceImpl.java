package mx.com.admoninmuebles.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.TipoTelefonoDto;
import mx.com.admoninmuebles.persistence.model.TipoTelefono;
import mx.com.admoninmuebles.persistence.repository.TipoTelefonoRepository;

@Service
public class TipoTelefonoServiceImpl implements TipoTelefonoService {

    @Autowired
    private TipoTelefonoRepository tipoTelefonoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TipoTelefono save(final TipoTelefonoDto tipoTelefonoDto) {
        return tipoTelefonoRepository.save(modelMapper.map(tipoTelefonoDto, TipoTelefono.class));
    }

}
