package mx.com.admoninmuebles.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.NotificacionDto;
import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.persistence.model.EstatusPago;
import mx.com.admoninmuebles.persistence.model.Pago;
import mx.com.admoninmuebles.persistence.repository.EstatusPagoRepository;
import mx.com.admoninmuebles.persistence.repository.PagoRepository;

@Service
public class NotificacionPagoServiceImpl implements NotificacionPagoService {
	
	private static final Long DIAS_PARA_VENCIMIENTO_PAGO = 5L;
	
	private static final Long DIAS_EXPOSICION_NOTIFICACION = 5L;
	
	@Autowired
	private NotificacionService notificacionService;
	
	@Autowired
	private InmuebleService inmuebleService;
	
    @Autowired
    private EstatusPagoRepository estatusPagoRepository;
    
    @Autowired
    private PagoRepository pagoRepository;
    
    @Autowired
    private MessageSource messages;
    
    @Transactional
    @Scheduled(cron = "${pagos.notificaciones.atrasados.cron.expresion}")
	@Override
	public void notificarMorosos() {
		
		EstatusPago estatusPagoAtrasado= estatusPagoRepository.findByNameAndLang( EstatusPago.ATRASADO, "es");
		
		LocalDate hoy = LocalDate.now();
		Collection<Pago> pagosAtrasados = pagoRepository.findByEstatusPagoId( estatusPagoAtrasado.getId() );
		
		pagosAtrasados.forEach( pago -> {
			
			LocalDate fechaCreacionLocalDate = pago.getFechaCreacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays( DIAS_PARA_VENCIMIENTO_PAGO );
			
			if( (fechaCreacionLocalDate.until(hoy).getDays() % DIAS_PARA_VENCIMIENTO_PAGO ) == 0 ) {
				notificarMoroso( pago );
			}
			
		});
		
	}

	@Override
	public void notificarPagoRealizado(PagoDto pagoDto) {
		
		InmuebleDto inmueble = inmuebleService.findBySocioId( pagoDto.getUsuarioId() );
    	notificarPagoAdministradorBi(pagoDto, inmueble.getAdminBiId(), inmueble.getAdminBiCorreo() );
    	notificarPagoContador(pagoDto, inmueble.getContadorId(), inmueble.getContadorCorreo() );
		
	}
	
	@Override
	public void notificarVerificacionPago(PagoDto pagoDto) {
		
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setFechaExposicionInicial( LocalDate.now() );
		notificacionDto.setFechaExposicionFinal( LocalDate.now().plusDays( DIAS_EXPOSICION_NOTIFICACION ) );
		notificacionDto.setUsuarioId( pagoDto.getUsuarioId() );
		notificacionDto.setTitulo( messages.getMessage("notificacion.pago.verificado.titulo" , null, Locale.getDefault()) );
		
		StringBuffer notificacionDesc = new StringBuffer(  messages.getMessage("notificacion.pago.verificado.descripcion" , null, Locale.getDefault())  );
		notificacionDesc.append("\n");
		notificacionDesc.append( messages.getMessage("notificacion.pago.concepto" , null, Locale.getDefault()) ).append(" ").append( pagoDto.getConcepto() );
		notificacionDesc.append("\n");
		notificacionDesc.append(messages.getMessage("notificacion.pago.monto" , null, Locale.getDefault()) ).append(" ").append( pagoDto.getMonto() );
		notificacionDesc.append("\n");
		notificacionDesc.append(messages.getMessage("notificacion.pago.tipopago" , null, Locale.getDefault() ) ).append(" ").append( pagoDto.getTipoPagoDescripction() );
		
		notificacionDto.setDescripcion( notificacionDesc.toString() );
		
		
		notificacionService.notificarUsuario(notificacionDto);
		
	}
	
	private void notificarMoroso( Pago pago ) {
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setFechaExposicionInicial( LocalDate.now() );
		notificacionDto.setFechaExposicionFinal( LocalDate.now().plusDays( DIAS_EXPOSICION_NOTIFICACION ) );
		notificacionDto.setUsuarioId( pago.getUsuario().getId() );
		notificacionDto.setTitulo( messages.getMessage("notificacion.morosos.recordatorio.pago.titulo" , null, Locale.getDefault()) );
		notificacionDto.setUsuarioCorreo( pago.getUsuario().getCorreo() );
		
		LocalDateTime fechavencimientoPago = pago.getFechaCreacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays( DIAS_PARA_VENCIMIENTO_PAGO );
		LocalDateTime fechaActual =  LocalDateTime.now();
		
		Long diasAtraso = fechavencimientoPago.until(fechaActual, ChronoUnit.DAYS );
		
		StringBuffer notificacionDesc = new StringBuffer(  messages.getMessage("notificacion.morosos.recordatorio.pago.descripcion" , null, Locale.getDefault())  );
		notificacionDesc.append("\n");
		notificacionDesc.append( messages.getMessage("notificacion.morosos.recordatorio.pago.concepto" , null, Locale.getDefault()) ).append(" ").append( pago.getConcepto() );
		notificacionDesc.append("\n");
		notificacionDesc.append(messages.getMessage("notificacion.morosos.recordatorio.pago.monto" , null, Locale.getDefault()) ).append(" ").append( pago.getMonto() );
		notificacionDesc.append("\n");
		notificacionDesc.append(messages.getMessage("notificacion.morosos.recordatorio.pago.atraso.dias" , null, Locale.getDefault() ) ).append(" ").append( diasAtraso );
		
		notificacionDto.setDescripcion( notificacionDesc.toString() );
		
		
		notificacionService.notificarUsuario(notificacionDto);
	}
	
	private void notificarPagoAdministradorBi( PagoDto pago, Long idAdminBiANotificar, String correo ) {
	    	notificarPagoRealizado(pago, idAdminBiANotificar, correo);
    }
    
    private void notificarPagoContador( PagoDto pago, Long idContadorANotificar, String correo ) {
    	notificarPagoRealizado(pago, idContadorANotificar,correo);
    }
    
    private void notificarPagoRealizado( PagoDto pagoDto, Long idUsuarioANotificar, String correo ) {
    	
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setUsuarioCorreo( correo );
		notificacionDto.setFechaExposicionInicial( LocalDate.now() );
		notificacionDto.setFechaExposicionFinal( LocalDate.now().plusDays( DIAS_EXPOSICION_NOTIFICACION ) );
		notificacionDto.setUsuarioId( idUsuarioANotificar );
		notificacionDto.setTitulo( messages.getMessage("notificacion.pago.realizado.titulo" , null, Locale.getDefault() ) );
		
		StringBuffer notificacionDesc = new StringBuffer(  messages.getMessage("notificacion.pago.realizado.descripcion" , null, Locale.getDefault() )  );
		notificacionDesc.append("\n");
		notificacionDesc.append( messages.getMessage("notificacion.pago.socio" , null, Locale.getDefault() ) ).append(" ").append( pagoDto.getUsuarioNombre() );
		notificacionDesc.append("\n");
		notificacionDesc.append( messages.getMessage("notificacion.pago.concepto" , null, Locale.getDefault() ) ).append(" ").append( pagoDto.getConcepto() );
		notificacionDesc.append("\n");
		notificacionDesc.append(messages.getMessage("notificacion.pago.monto" , null, Locale.getDefault() ) ).append(" ").append( pagoDto.getMonto() );
		notificacionDesc.append("\n");
		notificacionDesc.append(messages.getMessage("notificacion.pago.tipopago" , null, Locale.getDefault() ) ).append(" ").append( pagoDto.getTipoPagoDescripction() );
		
		notificacionDto.setDescripcion( notificacionDesc.toString() );
		
		notificacionService.notificarUsuario(notificacionDto);
    }

}
