package mx.com.admoninmuebles.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import mx.com.admoninmuebles.constant.PlantillaCorreoConst;
import mx.com.admoninmuebles.dto.CorreoDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.NotificacionDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Notificacion;
import mx.com.admoninmuebles.persistence.repository.NotificacionRepository;

@Service
public class NotificacionServiceImpl implements NotificacionService {
	
	Logger logger = LoggerFactory.getLogger(NotificacionServiceImpl.class);
	
	private final static String PROPIEDAD_CORREO_USUARIOS_DE = "usuario.cuenta.correo.de";

    @Autowired
    private NotificacionRepository notificacionRepository;
    
    @Autowired
    private InmuebleService inmuebleService;
    
    @Autowired
    private CorreoService correoService;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private Environment env;

    @Override
    public Notificacion save(final NotificacionDto notificacionDto) {
    	if( notificacionDto.getUsuarioId() != null ) {
    		notificacionDto.setInmuebleId(null);
		}
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
		
		return StreamSupport.stream(notificacionRepository.findByInmuebleIdAndFechaExposicionInicialLessThanEqualAndFechaExposicionFinalGreaterThanEqual(id, hoy, hoy).spliterator(), false)
				.map(notificacion -> modelMapper.map(notificacion, NotificacionDto.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<NotificacionDto> findBySocioIdtExpired(Long idSocio) {
		InmuebleDto inmueble = inmuebleService.findBySocioId( idSocio );
		LocalDate hoy = LocalDate.now();
		return StreamSupport.stream(notificacionRepository.findByInmuebleIdOrUsuarioIdAndFechaExposicionInicialLessThanEqualAndFechaExposicionFinalGreaterThanEqual(inmueble.getId(), idSocio, hoy, hoy)
				.spliterator(), false)
				.map(notificacion -> modelMapper.map(notificacion, NotificacionDto.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<NotificacionDto> findByUserIdNotExpired(Long idUsuario) {
		LocalDate hoy = LocalDate.now();
		return StreamSupport.stream(notificacionRepository.findByUsuarioIdAndFechaExposicionInicialLessThanEqualAndFechaExposicionFinalGreaterThanEqual(idUsuario, hoy, hoy)
				.spliterator(), false)
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

	@Override
	public Collection<NotificacionDto> findByAdminBiId(Long idAdminBi) {
		return StreamSupport.stream(notificacionRepository.findByAdminBiId(idAdminBi).spliterator(), false)
				.map(notificacion -> modelMapper.map(notificacion, NotificacionDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<NotificacionDto> findByAdminZonaId(Long idAdminZona) {
		return StreamSupport.stream(notificacionRepository.findByAdminZonaId(idAdminZona).spliterator(), false)
				.map(notificacion -> modelMapper.map(notificacion, NotificacionDto.class))
				.collect(Collectors.toList());
	}

	@Transactional
	@Async
	@Override
	public void notificarUsuario(NotificacionDto notificacionDto) {
		try {
			save(notificacionDto);
			
			Context datosPlantilla = new Context();
			datosPlantilla.setVariable( "mensaje", notificacionDto.getDescripcion()  );
			datosPlantilla.setVariable( "titulo", notificacionDto.getTitulo() );
			datosPlantilla.setVariable( "nombre", notificacionDto.getUsuarioNombre() );
			
			CorreoDto correoDto = new CorreoDto();
			correoDto.setDe( env.getProperty( PROPIEDAD_CORREO_USUARIOS_DE ) );
			correoDto.setAsunto( notificacionDto.getTitulo() );
			correoDto.setPlantilla( PlantillaCorreoConst.NOTIFICACION );
			correoDto.setPara( notificacionDto.getUsuarioCorreo());
			correoDto.setDatosPlantilla( datosPlantilla );
			
			correoService.enviarCorreo( correoDto );
		} catch( BusinessException e ) {
			logger.error("Error al enviar correo en la notificacion: ", e);
		} catch( Exception e ) {
			logger.error("Error al guardar la notificacion: ", e);
		}
	}
	
	@Transactional
	@Async
	@Override
	public void notificarInmueble(NotificacionDto notificacionDto) {
		
		try {
			save(notificacionDto);
			
			Collection<UsuarioDto> socios = inmuebleService.findSociosActivosByInmuebleId( notificacionDto.getInmuebleId() );
			
			Context datosPlantilla = new Context();
			datosPlantilla.setVariable( "mensaje", notificacionDto.getDescripcion()  );
			datosPlantilla.setVariable( "titulo", notificacionDto.getTitulo() );
			datosPlantilla.setVariable( "nombre", notificacionDto.getUsuarioNombre() );
			
			CorreoDto correoDto = new CorreoDto();
			correoDto.setDe( env.getProperty( PROPIEDAD_CORREO_USUARIOS_DE ) );
			correoDto.setAsunto( notificacionDto.getTitulo() );
			correoDto.setPlantilla( PlantillaCorreoConst.NOTIFICACION );
			correoDto.setDatosPlantilla( datosPlantilla );
			
			socios.stream().forEach( socio -> {
				correoDto.setPara( socio.getCorreo() );
				correoService.enviarCorreo( correoDto );
			});
			
		} catch( BusinessException e ) {
			logger.error("Error al enviar correo en la notificacion: ", e);
		} catch( Exception e ) {
			logger.error("Error al guardar la notificacion: ", e);
		}
	}

}
