package mx.com.admoninmuebles.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.NotificacionDto;
import mx.com.admoninmuebles.persistence.model.Notificacion;
import mx.com.admoninmuebles.persistence.repository.NotificacionRepository;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Notificacion save(final NotificacionDto notificacionDto) {
        return notificacionRepository.save(modelMapper.map(notificacionDto, Notificacion.class));
    }

	@Override
	public Collection<NotificacionDto> findAll() {
		return StreamSupport.stream(notificacionRepository.findAll().spliterator(), false)
				.map(notificacion -> modelMapper.map(notificacion, NotificacionDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public NotificacionDto findById(Long idNotificacion) {
		Optional<Notificacion> notificacion = notificacionRepository.findById(idNotificacion);
		return modelMapper.map(notificacion.get(), NotificacionDto.class);
	}

	@Override
	public void deleteById(Long idNotificacion) {
		notificacionRepository.deleteById(idNotificacion);
		
	}

	@Override
	public Collection<NotificacionDto> findByInmuebleId(Long id) {
		return StreamSupport.stream(notificacionRepository.findByInmuebleId(id).spliterator(), false)
				.map(notificacion -> modelMapper.map(notificacion, NotificacionDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<NotificacionDto> findByInmuebleIdNotExpired(Long id) {
		
		LocalDate hoy = LocalDate.now();
		
//		GregorianCalendar cal = new GregorianCalendar();
//		cal.setTime(hoy);
//		cal.add(Calendar.DATE, 1);
//		hoy =  cal.getTime();
		
		return StreamSupport.stream(notificacionRepository.findByInmuebleIdAndFechaExposicionInicialLessThanEqualAndFechaExposicionFinalGreaterThanEqual(id, hoy, hoy).spliterator(), false)
				.map(notificacion -> modelMapper.map(notificacion, NotificacionDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<NotificacionDto> findByZonaId(String id) {
		return StreamSupport.stream(notificacionRepository.findByZonaId(id).spliterator(), false)
				.map(notificacion -> modelMapper.map(notificacion, NotificacionDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<NotificacionDto> findByInmuebleAdminBiId(Long id) {
		return StreamSupport.stream(notificacionRepository.findByInmuebleAdminBiId(id).spliterator(), false)
				.map(notificacion -> modelMapper.map(notificacion, NotificacionDto.class))
				.collect(Collectors.toList());
	}

}
