package mx.com.admoninmuebles.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.persistence.model.Pago;
import mx.com.admoninmuebles.persistence.repository.PagoRepository;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Pago save(final PagoDto pagoDto) {
        return pagoRepository.save(modelMapper.map(pagoDto, Pago.class));
    }

}
