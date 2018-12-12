package mx.com.admoninmuebles.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.PrivilegioDto;
import mx.com.admoninmuebles.persistence.model.Privilegio;
import mx.com.admoninmuebles.persistence.repository.PrivilegioRepository;

@Service
public class PrivilegioServiceImpl implements PrivilegioService {

    @Autowired
    private PrivilegioRepository privilegioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Privilegio save(final PrivilegioDto privilegioDto) {
        return privilegioRepository.save(modelMapper.map(privilegioDto, Privilegio.class));
    }

}
