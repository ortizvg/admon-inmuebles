package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.ReservacionDto;
import mx.com.admoninmuebles.persistence.model.Reservacion;
import mx.com.admoninmuebles.persistence.repository.ReservacionRepository;

@Service
public class ReservacionServiceImpl implements ReservacionService {

    @Autowired
    private ReservacionRepository reservacionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Reservacion save(final ReservacionDto reservacionDto) {
        return reservacionRepository.save(modelMapper.map(reservacionDto, Reservacion.class));
    }

    @Override
    public Collection<ReservacionDto> findAll() {
        return StreamSupport.stream(reservacionRepository.findAll().spliterator(), false).map(reservacion -> modelMapper.map(reservacion, ReservacionDto.class)).collect(Collectors.toList());
    }

    @Override
    public Collection<ReservacionDto> findByAreaComun(final Long areaComunId) {
        return StreamSupport.stream(reservacionRepository.findByAreaComunId(areaComunId).spliterator(), false).map(reservacion -> modelMapper.map(reservacion, ReservacionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ReservacionDto> findByAreaComunAndInmueble(final Long areaComunId, final Long inmuebleId) {
        return StreamSupport.stream(reservacionRepository.findByAreaComunIdAndAreaComunInmuebleId(areaComunId, inmuebleId).spliterator(), false)
                .map(reservacion -> modelMapper.map(reservacion, ReservacionDto.class)).collect(Collectors.toList());
    }

    @Override
    public Collection<ReservacionDto> findBySocio(final Long socioId) {
        return StreamSupport.stream(reservacionRepository.findBySocioId(socioId).spliterator(), false).map(reservacion -> modelMapper.map(reservacion, ReservacionDto.class)).collect(Collectors.toList());
    }

    @Override
    public ReservacionDto findById(final Long id) {
        Optional<Reservacion> reservacion = reservacionRepository.findById(id);
        return modelMapper.map(reservacion.get(), ReservacionDto.class);
    }

    @Override
    public void delete(final Long id) {
        reservacionRepository.deleteById(id);
    }

}
