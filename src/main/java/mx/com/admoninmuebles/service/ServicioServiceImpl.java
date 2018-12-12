package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.ServicioDto;
import mx.com.admoninmuebles.persistence.model.Servicio;
import mx.com.admoninmuebles.persistence.repository.ServicioRepository;

@Service
public class ServicioServiceImpl implements ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Servicio save(final ServicioDto servicioDto) {
        return servicioRepository.save(modelMapper.map(servicioDto, Servicio.class));
    }

    @Override
    public Collection<ServicioDto> findAll() {
        return StreamSupport.stream(servicioRepository.findAll().spliterator(), false).map(servicio -> modelMapper.map(servicio, ServicioDto.class)).collect(Collectors.toList());
    }

    @Override
    public ServicioDto findById(final Long id) {
        Optional<Servicio> servicio = servicioRepository.findById(id);
        return modelMapper.map(servicio.get(), ServicioDto.class);
    }

    @Override
    public void deleteById(final Long id) {
        servicioRepository.deleteById(id);

    }

    @Override
    public boolean exist(final Long id) {
        return servicioRepository.existsById(id);
    }
}
