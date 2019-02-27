package mx.com.admoninmuebles.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.dto.PagoPaypalDto;
import mx.com.admoninmuebles.dto.PagoTarjetaDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Archivo;
import mx.com.admoninmuebles.persistence.model.EstatusPago;
import mx.com.admoninmuebles.persistence.model.Pago;
import mx.com.admoninmuebles.persistence.model.TipoPago;
import mx.com.admoninmuebles.persistence.model.TipoPagoBancario;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.repository.ArchivoRepository;
import mx.com.admoninmuebles.persistence.repository.EstatusPagoRepository;
import mx.com.admoninmuebles.persistence.repository.PagoRepository;
import mx.com.admoninmuebles.persistence.repository.TipoPagoBancarioRepository;
import mx.com.admoninmuebles.persistence.repository.TipoPagoRepository;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;
import mx.com.admoninmuebles.security.SecurityUtils;

@Service
public class PagoServiceImpl implements PagoService {
	
	private static final Logger logger = LoggerFactory.getLogger(PagoService.class);

    @Autowired
    private PagoRepository pagoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private TipoPagoRepository tipoPagoRepository;
    
    @Autowired
    private TipoPagoBancarioRepository tipoPagoBancarioRepository;
    
    @Autowired
    private EstatusPagoRepository estatusPagoRepository;
    
    @Autowired
    private ArchivoRepository archivoRepository;
    
    @Autowired
    private InmuebleService inmuebleService;
    
    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private NotificacionPagoService notificacionPagoService;

    @Autowired
    private MessageSource messages;
    
    @Override
    public PagoDto pagarTranferenciaBancaria(final PagoDto pagoDto, Locale locale) {
    	
    	Optional<Pago> pagoOpt = pagoRepository.findById( pagoDto.getId() );
        if (!pagoOpt.isPresent()) {
            throw new BusinessException("pago.error.noencontrado");
        }
    	
    	Archivo comprobantePagoCreado = null;
    	if(  pagoDto.getComprobantePagoMf() != null ) {
    		try {
    			Archivo archivo = new Archivo();
    			archivo.setId(UUID.randomUUID().toString());
    			archivo.setBytes(pagoDto.getComprobantePagoMf().getBytes());
    			archivo.setNombre(pagoDto.getComprobantePagoMf().getOriginalFilename());
    			archivo.setTipoContenido(pagoDto.getComprobantePagoMf().getContentType());
    			comprobantePagoCreado = archivoRepository.save(archivo);
    			
    		} catch (IOException e) {
    			throw new BusinessException("pago.error.carga.comprobante");
    		}
    	}
    	
    	
    	 Pago pago =  pagoOpt.get();
    	 
    	 pago.setTipoPagoBancario( tipoPagoBancarioRepository.findByNameAndLang( TipoPagoBancario.TRANSFERENCIA, "es" ) );
    	 pago.setEstatusPago( estatusPagoRepository.findByNameAndLang( EstatusPago.VERIFICACION, "es") );
    	 pago.setVerificado(false);
    	 pago.setFechaPago(LocalDate.now());
    	 pago.setComprobantePago(comprobantePagoCreado);
    	
    	 Pago pagoCreado =  pagoRepository.save( pago );
    	 PagoDto pagoCreadoDto = modelMapper.map(pagoCreado, PagoDto.class);
    	 
    	 notificacionPagoService.notificarPagoRealizado(pagoCreadoDto);
    	 
    	 return pagoCreadoDto;
    }
    
//    private void notificarPago( Pago pago, Locale locale ) {
//    	InmuebleDto inmueble = inmuebleService.findBySocioId( pago.getUsuario().getId() );
//    	notificarPagoAdministradorBi(pago, locale, inmueble.getAdminBiId() );
//    	notificarPagoContador(pago, locale, inmueble.getContadorId() );
//    }
//    
//    private void notificarPagoAdministradorBi( Pago pago, Locale locale, Long idAdminBiANotificar ) {
//    	notificar(pago, locale, idAdminBiANotificar);
//    }
//    
//    private void notificarPagoContador( Pago pago, Locale locale, Long idContadorANotificar ) {
//    	notificar(pago, locale, idContadorANotificar);
//    }
//    
//    private void notificar( Pago pago, Locale locale, Long idUsuarioANotificar ) {
//    	
//		final Long DIAS_EXPOSICION_NOTIFICACION = 5L;
//		
//		NotificacionDto notificacionDto = new NotificacionDto();
//		notificacionDto.setFechaExposicionInicial( LocalDate.now() );
//		notificacionDto.setFechaExposicionFinal( LocalDate.now().plusDays( DIAS_EXPOSICION_NOTIFICACION ) );
//		notificacionDto.setUsuarioId( idUsuarioANotificar );
//		notificacionDto.setTitulo( messages.getMessage("pago.notificacion.titulo" , null, locale) );
//		
//		StringBuffer notificacionDesc = new StringBuffer(  messages.getMessage("pago.notificacion.descripcion" , null, locale)  );
//		notificacionDesc.append("\n");
//		notificacionDesc.append( messages.getMessage("pago.notificacion.socio" , null, locale) ).append(" ").append( pago.getUsuario().getNombre() );
//		notificacionDesc.append("\n");
//		notificacionDesc.append( messages.getMessage("pago.notificacion.concepto" , null, locale) ).append(" ").append( pago.getConcepto() );
//		notificacionDesc.append("\n");
//		notificacionDesc.append(messages.getMessage("pago.notificacion.monto" , null, locale) ).append(" ").append( pago.getMonto() );
//		notificacionDesc.append("\n");
//		notificacionDesc.append(messages.getMessage("pago.notificacion.tipopago" , null, locale) ).append(" ").append( pago.getTipoPago().getDescripction() );
//		
//		notificacionDto.setDescripcion( notificacionDesc.toString() );
//    }
    
    @Override
    public PagoDto pagarPaypal(final PagoPaypalDto pagoPaypalDto) {
    	 throw new UnsupportedOperationException();
    }
    
    @Override
    public PagoDto pagarTarjeta(final PagoTarjetaDto pagoTarjetaDto) {
    	 throw new UnsupportedOperationException();
    }

    @Override
    public PagoDto guardar(final PagoDto pagoDto) {
        Pago pagoNuevo =  pagoRepository.save(modelMapper.map(pagoDto, Pago.class));
        return modelMapper.map(pagoNuevo, PagoDto.class);
    }
    
    @Override
    public PagoDto verificar(final Long idPago) {
    	Optional<Pago> pagoOpt = pagoRepository.findById( idPago );
        if (!pagoOpt.isPresent()) {
            throw new BusinessException("pago.error.noencontrado");
        }
        
        Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
        Usuario usuarioVerificador = usuarioRepository.findById( usuarioLogueadoId ).get();
        
        Pago pagoVerificado = pagoOpt.get();
        pagoVerificado.setVerificado(true);
        pagoVerificado.setFechaVerificacion(new Date());
        pagoVerificado.setEstatusPago( estatusPagoRepository.findByNameAndLang( EstatusPago.PAGADO, "es") );
        pagoVerificado.setUsuarioVerificador(usuarioVerificador);
        
        Pago pagoActualizado =  pagoRepository.save( pagoVerificado );
        
        PagoDto pagoActualizadoDto =  modelMapper.map( pagoActualizado , PagoDto.class );
        
        notificacionPagoService.notificarVerificacionPago( pagoActualizadoDto );
        
        return pagoActualizadoDto;
    }
    
    @Override
    public Collection<PagoDto> buscarTodo() {
		return StreamSupport.stream(pagoRepository.findAll().spliterator(), false)
				.map(pago -> modelMapper.map(pago, PagoDto.class))
				.collect(Collectors.toList());
    }
    
    @Override
    public PagoDto buscarId(Long idPago) {
    	Optional<Pago> pagoOpt = pagoRepository.findById( idPago );
        if (!pagoOpt.isPresent()) {
            throw new BusinessException("pago.error.noencontrado");
        }
        return modelMapper.map( pagoOpt.get() , PagoDto.class );
    }
    
    @Override
    public boolean existePago(Long idPago) {
    	Optional<Pago> pagoOpt = pagoRepository.findById( idPago );
        if (pagoOpt.isPresent()) {
            return true;
        }
        return false;
    }

	@Override
	public Collection<PagoDto> buscarPorUsuario(Long idUsuario) {
		return StreamSupport.stream(pagoRepository.findByUsuarioId( idUsuario ).spliterator(), false)
				.map(pago -> modelMapper.map(pago, PagoDto.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<PagoDto> buscarPorInmueble(Long idInmueble) {
		return StreamSupport.stream(pagoRepository.findByInmuebleId( idInmueble ).spliterator(), false)
				.map(pago -> modelMapper.map(pago, PagoDto.class))
				.collect(Collectors.toList());
		
	}
	
	@Override
	public Collection<PagoDto> buscarPorCodigoZona( String codigoZona ) {
		return StreamSupport.stream(pagoRepository.findBycodigoZona( codigoZona ).spliterator(), false)
				.map(pago -> modelMapper.map(pago, PagoDto.class))
				.collect(Collectors.toList());
		
	}
	
	@Override
	public Collection<PagoDto> buscarPorContador(Long idContador) {
		return StreamSupport.stream(pagoRepository.findByContadorId( idContador ).spliterator(), false)
				.map(pago -> modelMapper.map(pago, PagoDto.class))
				.collect(Collectors.toList());
	}

	@Scheduled(cron = "${pagos.ordinario.cron.expresion}")
	@Override
	public void generarPagosMensuales() {
		Collection<Usuario> sociosActivos = usuarioRepository.findByRolesNombreAndActivo(RolConst.ROLE_SOCIO_BI, true);
		TipoPago tipoPago = tipoPagoRepository.findByNameAndLang( TipoPago.CUOTA, "es" );
		EstatusPago estatusPagoCercano = estatusPagoRepository.findByNameAndLang( EstatusPago.CERCANO, "es");
		sociosActivos.forEach( socio -> {
			Pago pago = new Pago();
			pago.setTipoPago( tipoPago );
			pago.setEstatusPago( estatusPagoCercano );
			pago.setReferencia(socio.getReferenciaPagoSocio());
			pago.setNumeroCuenta(socio.getCuentaPagoSocio());
			pago.setMonto(socio.getCoutaMensualPagoSocio());
			pago.setUsuario(socio);
			pago.setVerificado(false);
			
			pagoRepository.save(pago);
			
		});
	}
	
	@Scheduled(cron = "${pagos.atrasados.cron.expresion}")
	@Override
	public void actualizarEstatusPagosMensuales() {
		
		EstatusPago estatusPagoCercano =  estatusPagoRepository.findByNameAndLang( EstatusPago.CERCANO, "es");
		EstatusPago estatusPagoAtrasado= estatusPagoRepository.findByNameAndLang( EstatusPago.ATRASADO, "es");
		
		LocalDate hoy = LocalDate.now();
		Collection<Pago> pagosCercanos = pagoRepository.findByEstatusPagoId( estatusPagoCercano.getId() );
		
		pagosCercanos.forEach( pago -> {
			
			LocalDate fechaCreacionLocalDate = pago.getFechaCreacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(5L);
			
			if( fechaCreacionLocalDate.isBefore( hoy ) ) {
				pago.setEstatusPago( estatusPagoAtrasado );
				pagoRepository.save(pago);
			}
			
		});
		
	}

	@Override
	public void generarPagos(PagoDto pagoDto) {	
		
		if( pagoDto.getUsuarioId() != null ) {
			generarPagosPorSocio( pagoDto );
		} else if ( pagoDto.getInmuebleId() != null ) {
			generarPagosPorInmueble( pagoDto );
		} else {
			 throw new BusinessException("pago.error.generacion.campos.obligatorios");
		}
		
	}
	
	private void generarPagosPorInmueble(PagoDto pagoDto) {
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		Usuario usuarioGenerador = usuarioRepository.findById( usuarioLogueadoId ).get();
		
		Collection<UsuarioDto> sociosActivos = inmuebleService.findSociosActivosByInmuebleId( pagoDto.getInmuebleId() );
		TipoPago tipoPago = tipoPagoRepository.findById( pagoDto.getTipoPagoId()).get();
		EstatusPago estatusPagoCercano =  estatusPagoRepository.findByNameAndLang( EstatusPago.CERCANO, "es");
		sociosActivos.forEach( socio -> {
			Pago pago = new Pago();
			pago.setTipoPago( tipoPago );
			pago.setEstatusPago(estatusPagoCercano );
			pago.setReferencia(socio.getReferenciaPagoSocio());
			pago.setNumeroCuenta(socio.getCuentaPagoSocio());
			pago.setMonto(pagoDto.getMonto());
			pago.setConcepto(pagoDto.getConcepto());
			pago.setVerificado(false);
			pago.setUsuarioGenerador(usuarioGenerador);
//			Usuario usuario = new Usuario();
//			socio.setId();
			pago.setUsuario( usuarioRepository.findById(socio.getId()).get() );
			
			pagoRepository.save(pago);
		});
		
	}
	
	@Override
	public PagoDto generarPagosPorSocio(PagoDto pagoDto) {
		
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		Usuario usuarioGenerador = usuarioRepository.findById( usuarioLogueadoId ).get();
		
		Usuario socio = usuarioRepository.findById( pagoDto.getUsuarioId() ).get();
		TipoPago tipoPago = tipoPagoRepository.findById( pagoDto.getTipoPagoId()).get();
		EstatusPago estatusPagoCercano =  estatusPagoRepository.findByNameAndLang( EstatusPago.CERCANO, "es");
		
		Pago pago = new Pago();
		pago.setTipoPago( tipoPago );
		pago.setEstatusPago(estatusPagoCercano );
		pago.setReferencia(socio.getReferenciaPagoSocio());
		pago.setNumeroCuenta(socio.getCuentaPagoSocio());
		pago.setMonto(pagoDto.getMonto());
		pago.setConcepto(pagoDto.getConcepto());
		pago.setVerificado(false);
		pago.setUsuario( socio );
		pago.setUsuarioGenerador(usuarioGenerador);
		
		PagoDto pageGenerado =  modelMapper.map( pagoRepository.save(pago) , PagoDto.class );
				
		notificacionPagoService.notificarGeneracionPago( pageGenerado );
		
		return pageGenerado;
	}

	@Override
	public void eliminarPorId(Long idPago) {
		pagoRepository.deleteById(idPago);
	}

	@Override
	public Collection<PagoDto> buscarPorAdminBi(Long idAdminBi) {
		return StreamSupport.stream(pagoRepository.findByAdminBiId( idAdminBi ).spliterator(), false)
				.map(pago -> modelMapper.map(pago, PagoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<PagoDto> buscarPorAdminZona(Long idAdminZona) {
		return StreamSupport.stream(pagoRepository.findByAdminZonaId( idAdminZona ).spliterator(), false)
				.map(pago -> modelMapper.map(pago, PagoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<PagoDto> buscarPorInmuebleYEstatusPagoNombreYTipoPagoNombre(Long idInmueble, String estatusPagoNombre, String tipoPagoNombre) {
		return StreamSupport.stream(pagoRepository.findByInmuebleIdAndEstatusPagoNameAndTipoPagoName( idInmueble, estatusPagoNombre,  tipoPagoNombre).spliterator(), false)
				.map(pago -> modelMapper.map(pago, PagoDto.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<PagoDto> buscarPorInmuebleYEstatusPagoNombre(Long idInmueble, String nombre) {
		return StreamSupport.stream(pagoRepository.findByInmuebleIdAndEstatusPagoName( idInmueble, nombre ).spliterator(), false)
				.map(pago -> modelMapper.map(pago, PagoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Long getTotalPagosPorInmueble(Long idInmueble) {
		return pagoRepository.countByInmuebleId( idInmueble );
	}

	@Override
	public Long getTotalPagosPorInmuebleYEstatusPagoNOmbre(Long idInmueble, String nombre) {
		return pagoRepository.countByInmuebleIdAndEstatusPagoName( idInmueble, nombre );
	}
	
	@Override
	public Long getTotalPagosPorInmuebleYEstatusPagoNombreYTipoPagoNombre(Long idInmueble, String estatusPagoNombre, String tipoPagoNombre) {
		return pagoRepository.countByInmuebleIdAndEstatusPagoNameAndTipoPagoName( idInmueble, estatusPagoNombre,  tipoPagoNombre);
	}

	@Override
	public Collection<PagoDto> buscarPorSocioYEstatusPagoNombre(Long idSocio, String nombre) {
		return StreamSupport.stream(pagoRepository.findByInmuebleIdAndEstatusPagoName( idSocio, nombre ).spliterator(), false)
				.map(pago -> modelMapper.map(pago, PagoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<PagoDto> buscarTodoPorEstatus(Long idEstatus) {
		return StreamSupport.stream(pagoRepository.findByEstatusPagoId( idEstatus ).spliterator(), false)
				.map(pago -> modelMapper.map(pago, PagoDto.class))
				.collect(Collectors.toList());
	}


}
