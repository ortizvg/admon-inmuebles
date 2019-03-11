package mx.com.admoninmuebles.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.constant.ComunConst;
import mx.com.admoninmuebles.dto.ReservacionDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.AreaComun;
import mx.com.admoninmuebles.persistence.model.EstatusPago;
import mx.com.admoninmuebles.persistence.model.Reservacion;
import mx.com.admoninmuebles.persistence.repository.AreaComunRepository;
import mx.com.admoninmuebles.persistence.repository.PagoRepository;
import mx.com.admoninmuebles.persistence.repository.ReservacionRepository;
import mx.com.admoninmuebles.util.UtilDate;

@Service
public class ReservacionServiceImpl implements ReservacionService {
	
	Logger logger = LoggerFactory.getLogger(ReservacionServiceImpl.class);

    @Autowired
    private ReservacionRepository reservacionRepository;
    
    @Autowired
    private PagoRepository pagoRepository;
    
    @Autowired
    private AreaComunRepository areaComunRepository;
    
    @Autowired
    private NotificacionReservacionService notificacionReservacionService;

    @Autowired
    private ModelMapper modelMapper;
    
    @Value( "${reservas.pago.horas.tolerancia}" )
    private Long RESERVA_PAGO_TIEMPO_TOLERANICA_HORAS;
    
    

    @Transactional
    @Override
    public Reservacion save(final ReservacionDto reservacionDto) {
//    	Optional<AreaComun> areaComunOpt = areaComunRepository.findById( reservacionDto.getAreaComunId() );
    	reservacionDto.setTitle( reservacionDto.getTitle() + ComunConst.SEPARADOR_GUION_MEDIO + reservacionDto.getAreaComunNombre() );
    	Reservacion reservacionCreada = reservacionRepository.save(modelMapper.map(reservacionDto, Reservacion.class));
    	notificacionReservacionService.notificarReservacion(modelMapper.map(reservacionCreada, ReservacionDto.class));
    	
    	return reservacionCreada;
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
		
		LocalDate ldAnterior = LocalDate.parse(reservacionDto.getStart()).minusDays(1);
		LocalDate ldSiguiente = LocalDate.parse(reservacionDto.getStart()).plusDays(1);
		
		Collection<Reservacion> reservaciones = reservacionRepository.findByAreaComunIdAndSocioIdAndStartLessThanEqualAndStartGreaterThanEqual(reservacionDto.getAreaComunId(), reservacionDto.getSocioId(), 
						ldSiguiente.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), ldAnterior.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		
		if(reservaciones!=null && !reservaciones.isEmpty()) {
			throw new BusinessException("reserva.areacomun.validacion.mensaje.diaanteriorposterior");
		}
		
//		DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//		LocalDate ldAnterior = LocalDate.parse(reservacionDto.getStart(), FOMATTER).minusDays(1);
//		LocalDate ldSiguiente = LocalDate.parse(reservacionDto.getStart(), FOMATTER).plusDays(1);
//		
//		Collection<Reservacion> reservaciones = reservacionRepository.findByAreaComunIdAndSocioIdAndStartLessThanEqualAndStartGreaterThanEqual(reservacionDto.getAreaComunId(), reservacionDto.getSocioId(), 
//						ldSiguiente.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), ldAnterior.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//		
//		if(reservaciones!=null && !reservaciones.isEmpty()) {
//			throw new BusinessException("reserva.areacomun.validacion.mensaje.diaanteriorposterior");
//		}
		
	}
	
	@Scheduled(cron = "${reservas.nopagadas.borrado.cron.expresion}")
	@Override
	public void deleteNoPaidReservations() {
		
		Collection<Reservacion> reservaciones = null;
		try {
			reservaciones = reservacionRepository.findByPagoEstatusPagoNameAndPagoEstatusPagoLang(EstatusPago.CERCANO, "es"); 
			
			for( Reservacion reservacion: reservaciones ) {
				LocalDateTime reservationDate = UtilDate.dateToLocalDateTime(reservacion.getFechaCreacion());
							
				long hoursSinceReservation = ChronoUnit.HOURS.between(reservationDate, LocalDateTime.now());
				if( hoursSinceReservation >= RESERVA_PAGO_TIEMPO_TOLERANICA_HORAS ) {
					pagoRepository.deleteById(reservacion.getPago().getId());
					reservacionRepository.deleteById(reservacion.getId());
				}
			}
		} catch (Exception e) {
			logger.error("Hubo un error al eliminar reservaciones sin pago");
		}
		
	}
	
	public static void main(String[] args) {
		LocalDateTime reservationDate = new Date()
				.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		
		LocalDateTime now = LocalDateTime.now();
		
		long hoursSinceReservation = reservationDate.until(now, ChronoUnit.HOURS);
		
		System.out.println( hoursSinceReservation );
	}
	
}
