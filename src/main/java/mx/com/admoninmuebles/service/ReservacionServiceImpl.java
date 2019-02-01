package mx.com.admoninmuebles.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.ReservacionDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Reservacion;
import mx.com.admoninmuebles.persistence.repository.PagoRepository;
import mx.com.admoninmuebles.persistence.repository.ReservacionRepository;

@Service
public class ReservacionServiceImpl implements ReservacionService {

    @Autowired
    private ReservacionRepository reservacionRepository;
    
    @Autowired
    private PagoRepository pagoRepository;

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

	@Override
	public void validateReservation(ReservacionDto reservacionDto) {
		
		System.out.println("SOCIO: " + reservacionDto.getSocioId());
		System.out.println("AREA COMUN: " + reservacionDto.getAreaComunId());
		System.out.println("FECHA: " + reservacionDto.getStart());
		
		LocalDate diaAnterior = reservacionDto.getStart().minusDays(1);
		LocalDate diasiguiente = reservacionDto.getStart().plusDays(1);
		
		Collection<Reservacion> reservaciones = reservacionRepository.
				findByAreaComunIdAndSocioIdAndStartLessThanEqualAndStartGreaterThanEqual(reservacionDto.getAreaComunId(), reservacionDto.getSocioId(), diasiguiente, diaAnterior);
		
		System.out.println("RESERVACIONES: " + reservaciones.size());
		if(reservaciones.size() > 0) {
			throw new BusinessException("reserva.areacomun.validacion.mensaje.diaanteriorposterior");
		}
		
	}
	
	@Scheduled(cron = "${reservas.nopagadas.borrado.cron.expresion}")
	@Override
	public void deleteNoPaidReservations() {
		
		Collection<Reservacion> reservaciones = reservacionRepository.findAll(); 
		LocalDate now = LocalDate.now();
		
		for( Reservacion reservacion: reservaciones ) {
			LocalDate reservationDatePlus48Hours = reservacion.getFechaCreacion().toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDate()
			.plus(72, ChronoUnit.HOURS);
			
			if(now.isBefore(reservationDatePlus48Hours) ) {
				pagoRepository.deleteById(reservacion.getId());
				reservacionRepository.deleteById(reservacion.getId());
			}
		}
		
		
	}

}
