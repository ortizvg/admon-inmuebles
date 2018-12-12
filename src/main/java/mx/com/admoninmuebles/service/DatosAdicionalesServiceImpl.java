package mx.com.admoninmuebles.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.DatosAdicionalesDto;
import mx.com.admoninmuebles.persistence.model.DatosAdicionales;
import mx.com.admoninmuebles.persistence.repository.DatosAdicionalesRepository;

@Service
public class DatosAdicionalesServiceImpl implements DatosAdicionalesService {

    @Autowired
    private DatosAdicionalesRepository datosAdicionalesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DatosAdicionales save(final DatosAdicionalesDto datosAdicionalesDto) {
        return datosAdicionalesRepository.save(modelMapper.map(datosAdicionalesDto, DatosAdicionales.class));
    }

}
