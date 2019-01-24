package mx.com.admoninmuebles.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.dto.PagoPaypalDto;
import mx.com.admoninmuebles.dto.PagoTarjetaDto;
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
    private ModelMapper modelMapper;
    
    @Override
    public PagoDto pagarTranferenciaBancaria(final PagoDto pagoDto) {
    	
    	if(  pagoDto.getComprobantePagoMf() != null ) {
    		try {
    			pagoDto.setComprobantePago(pagoDto.getComprobantePagoMf().getBytes());
    			pagoDto.setComprobantePagoUrl(pagoDto.getComprobantePagoMf().getOriginalFilename());
    			
    			Archivo archivo = new Archivo();
    			archivo.setId(UUID.randomUUID().toString());
    			archivo.setBytes(pagoDto.getComprobantePagoMf().getBytes());
    			archivo.setNombre(pagoDto.getComprobantePagoMf().getOriginalFilename());
    			archivo.setTipoContenido(pagoDto.getComprobantePagoMf().getContentType());
    			Archivo archivoCreado = archivoRepository.save(archivo);
    			
    			logger.info( archivoCreado.getId().toString() );
    		} catch (IOException e) {
    			throw new BusinessException("pago.error.carga.comprobante");
    		}
    	}
    	
    	
    	 Pago pago =  modelMapper.map(pagoDto, Pago.class);
    	 
    	 pago.setTipoPagoBancario( tipoPagoBancarioRepository.findByNameAndLang( TipoPagoBancario.TRANSFERENCIA, "es" ) );
    	 pago.setEstatusPago( estatusPagoRepository.findByNameAndLang( EstatusPago.VERIFICACION, "es") );
    	 pago.setVerificado(false);
    	 pago.setFechaPago(LocalDate.now());
    	
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

	@Scheduled(cron = "${pagos.ordinario.cron.expresion}")
	@Override
	public void generarPagosMensuales() {
		Collection<Usuario> sociosActivos = usuarioRepository.findByRolesNombreAndActivo(RolConst.ROLE_SOCIO_BI, true);
		sociosActivos.forEach( socio -> {
			Pago pago = new Pago();
			pago.setTipoPago( tipoPagoRepository.findByNameAndLang( TipoPago.RENTA, "es" ) );
			pago.setEstatusPago( estatusPagoRepository.findByNameAndLang( EstatusPago.CERCANO, "es") );
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
		
		TipoPago tipoPagoOrdinario = tipoPagoRepository.findByNameAndLang( TipoPago.RENTA, "es" );
		EstatusPago estatusPagoCercano =  estatusPagoRepository.findByNameAndLang( EstatusPago.CERCANO, "es");
		
		Collection<Pago> pagosCercanos = pagoRepository.findByTipoPagoIdAndEstatusPagoId( tipoPagoOrdinario.getId(), estatusPagoCercano.getId() );
		pagosCercanos.forEach( pago -> {
			pago.setEstatusPago( estatusPagoRepository.findByNameAndLang( EstatusPago.ATRASADO, "es") );
			pagoRepository.save(pago);
			
		});
	}


}
