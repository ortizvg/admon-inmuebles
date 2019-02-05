package mx.com.admoninmuebles.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    private ModelMapper modelMapper;
    
    @Override
    public PagoDto pagarTranferenciaBancaria(final PagoDto pagoDto) {
    	
    	Archivo comprobantePagoCreado = null;
    	if(  pagoDto.getComprobantePagoMf() != null ) {
    		try {
//    			pagoDto.setComprobantePago(pagoDto.getComprobantePagoMf().getBytes());
//    			pagoDto.setComprobantePagoUrl(pagoDto.getComprobantePagoMf().getOriginalFilename());
    			
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
    	
    	
    	 Pago pago =  modelMapper.map(pagoDto, Pago.class);
    	 
    	 pago.setTipoPagoBancario( tipoPagoBancarioRepository.findByNameAndLang( TipoPagoBancario.TRANSFERENCIA, "es" ) );
    	 pago.setEstatusPago( estatusPagoRepository.findByNameAndLang( EstatusPago.VERIFICACION, "es") );
    	 pago.setVerificado(false);
    	 pago.setFechaPago(LocalDate.now());
    	 pago.setComprobantePago(comprobantePagoCreado);
    	
    	 Pago pagoCreado =  pagoRepository.save( pago );
    	 
    	 return modelMapper.map(pagoCreado, PagoDto.class);
    }
    
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
        
        Pago pagoVerificado = pagoOpt.get();
        pagoVerificado.setVerificado(true);
        pagoVerificado.setEstatusPago( estatusPagoRepository.findByNameAndLang( EstatusPago.PAGADO, "es") );
        
        Pago pagoActualizado =  pagoRepository.save( pagoVerificado );
        return modelMapper.map( pagoActualizado , PagoDto.class );
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
	
	@Async
	@Scheduled(cron = "${pagos.atrasados.cron.expresion}")
	@Override
	public void actualizarEstatusPagosMensuales() {
		
//		TipoPago tipoPagoOrdinario = tipoPagoRepository.findByNameAndLang( TipoPago.RENTA, "es" );
		EstatusPago estatusPagoCercano =  estatusPagoRepository.findByNameAndLang( EstatusPago.CERCANO, "es");
		EstatusPago estatusPagoAtrasado= estatusPagoRepository.findByNameAndLang( EstatusPago.ATRASADO, "es");
		
//		Collection<Pago> pagosCercanos = pagoRepository.findByTipoPagoIdAndEstatusPagoId( tipoPagoOrdinario.getId(), estatusPagoCercano.getId() );
		Collection<Pago> pagosCercanos = pagoRepository.findByEstatusPagoId( estatusPagoCercano.getId() );
		pagosCercanos.forEach( pago -> {
			
			if( pago.getFechaCreacion().after( new Date() ) ) {
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
//			Usuario usuario = new Usuario();
//			socio.setId();
			pago.setUsuario( usuarioRepository.findById(socio.getId()).get() );
			
			pagoRepository.save(pago);
		});
		
	}
	
	@Override
	public PagoDto generarPagosPorSocio(PagoDto pagoDto) {
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
		
		return modelMapper.map( pagoRepository.save(pago) , PagoDto.class );
	}

	@Override
	public void eliminarPorId(Long idPago) {
		pagoRepository.deleteById(idPago);
	}


}
