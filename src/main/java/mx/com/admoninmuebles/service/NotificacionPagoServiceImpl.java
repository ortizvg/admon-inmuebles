package mx.com.admoninmuebles.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.constant.ComunConst;
import mx.com.admoninmuebles.constant.LocaleConst;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.NotificacionDto;
import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.persistence.model.EstatusPago;
import mx.com.admoninmuebles.persistence.model.Pago;
import mx.com.admoninmuebles.persistence.repository.EstatusPagoRepository;
import mx.com.admoninmuebles.persistence.repository.PagoRepository;
import mx.com.admoninmuebles.security.SecurityUtils;

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
    private UsuarioService usuarioService;
    
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
				notificarMorosoUsuario( pago );
			}
			
		});
		
	}
    
    
    @Async
    @Transactional
	@Override
	public void notificarMorososPorInmueble(Long idInmueble) {
		
    	LocalDate hoy = LocalDate.now();
		Collection<Pago> pagosAtrasados = pagoRepository.findByInmuebleIdAndEstatusPagoName( idInmueble, EstatusPago.ATRASADO );
		InmuebleDto inmueble = inmuebleService.findById( idInmueble );
		
		pagosAtrasados.forEach( pago -> {
			
			LocalDate fechaCreacionLocalDate = pago.getFechaCreacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays( DIAS_PARA_VENCIMIENTO_PAGO );
			
			if( (fechaCreacionLocalDate.until(hoy).getDays() % DIAS_PARA_VENCIMIENTO_PAGO ) == 0 ) {
				notificarMorosoUsuario( pago );
				notificarMorosoAdminBi( pago, inmueble);
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
		
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		UsuarioDto usuarioVerificador = usuarioService.findById( usuarioLogueadoId );
		
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setFechaExposicionInicial( LocalDate.now() );
		notificacionDto.setFechaExposicionFinal( LocalDate.now().plusDays( DIAS_EXPOSICION_NOTIFICACION ) );
		notificacionDto.setUsuarioId( pagoDto.getUsuarioId() );
		notificacionDto.setUsuarioCorreo(pagoDto.getUsuarioCorreo());
		
		StringBuffer titulo = new StringBuffer(   messages.getMessage("notificacion.pago.verificado.titulo" , null, LocaleConst.LOCALE_MX )  );
		titulo.append( ComunConst.CADENA_ESPACIO );
		titulo.append( usuarioVerificador.getNombreCompleto() );
		
		notificacionDto.setTitulo( titulo.toString() );
		
		StringBuffer notificacionDesc = new StringBuffer(  messages.getMessage("notificacion.pago.verificado.descripcion" , null, LocaleConst.LOCALE_MX)  );
		notificacionDesc.append("\n");
		notificacionDesc.append( messages.getMessage("notificacion.pago.concepto" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( pagoDto.getConcepto() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.pago.monto" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( pagoDto.getMonto() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.pago.tipopago" , null, LocaleConst.LOCALE_MX ) ).append( ComunConst.CADENA_ESPACIO ).append( pagoDto.getTipoPagoDescripction() );
		
		notificacionDto.setDescripcion( notificacionDesc.toString() );
		
		
		notificacionService.notificarUsuario(notificacionDto);
		
	}
	
	@Override
	public void notificarGeneracionPago(PagoDto pagoDto) {
		
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		UsuarioDto usuarioGenerador = usuarioService.findById( usuarioLogueadoId );
		
		
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setFechaExposicionInicial( LocalDate.now() );
		notificacionDto.setFechaExposicionFinal( LocalDate.now().plusDays( DIAS_EXPOSICION_NOTIFICACION ) );
		notificacionDto.setUsuarioId( pagoDto.getUsuarioId() );
		notificacionDto.setUsuarioCorreo(pagoDto.getUsuarioCorreo());
		
		StringBuffer titulo = new StringBuffer(   messages.getMessage("notificacion.pago.generado.titulo" , null, LocaleConst.LOCALE_MX )  );
		titulo.append( ComunConst.CADENA_ESPACIO );
		titulo.append( usuarioGenerador.getNombreCompleto() );
		
		notificacionDto.setTitulo( titulo.toString() );
		
		StringBuffer notificacionDesc = new StringBuffer(  messages.getMessage("notificacion.pago.generado.descripcion" , null, LocaleConst.LOCALE_MX)  );
		notificacionDesc.append("\n");
		notificacionDesc.append( messages.getMessage("notificacion.pago.concepto" , null, LocaleConst.LOCALE_MX) ).append(ComunConst.CADENA_ESPACIO).append( pagoDto.getConcepto() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.pago.monto" , null, LocaleConst.LOCALE_MX) ).append(ComunConst.CADENA_ESPACIO).append( pagoDto.getMonto() );
		
		notificacionDto.setDescripcion( notificacionDesc.toString() );
		
		
		notificacionService.notificarUsuario(notificacionDto);
		
	}
	
	private void notificarMorosoUsuario( Pago pago ) {
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setFechaExposicionInicial( LocalDate.now() );
		notificacionDto.setFechaExposicionFinal( LocalDate.now().plusDays( DIAS_EXPOSICION_NOTIFICACION ) );
		notificacionDto.setUsuarioId( pago.getUsuario().getId() );
		notificacionDto.setTitulo( messages.getMessage("notificacion.morosos.recordatorio.pago.titulo" , null, LocaleConst.LOCALE_MX) );
		notificacionDto.setUsuarioCorreo( pago.getUsuario().getCorreo() );
		
		LocalDateTime fechavencimientoPago = pago.getFechaCreacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays( DIAS_PARA_VENCIMIENTO_PAGO );
		LocalDateTime fechaActual =  LocalDateTime.now();
		
		Long diasAtraso = fechavencimientoPago.until(fechaActual, ChronoUnit.DAYS );
		
		StringBuffer notificacionDesc = new StringBuffer();
		notificacionDesc.append( messages.getMessage("notificacion.pago.concepto" , null, LocaleConst.LOCALE_MX) ).append(ComunConst.CADENA_ESPACIO).append( pago.getConcepto() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.pago.monto" , null, LocaleConst.LOCALE_MX) ).append(ComunConst.CADENA_ESPACIO).append( pago.getMonto() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.morosos.recordatorio.pago.atraso.dias" , null, LocaleConst.LOCALE_MX ) ).append(ComunConst.CADENA_ESPACIO).append( diasAtraso );
		
		notificacionDto.setDescripcion( notificacionDesc.toString() );
		
		
		notificacionService.notificarUsuario(notificacionDto);
	}
	
	private void notificarMorosoAdminBi( Pago pago, InmuebleDto inmueble ) {
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setFechaExposicionInicial( LocalDate.now() );
		notificacionDto.setFechaExposicionFinal( LocalDate.now().plusDays( DIAS_EXPOSICION_NOTIFICACION ) );
		notificacionDto.setUsuarioId( inmueble.getAdminBiId() );
		notificacionDto.setTitulo( messages.getMessage("notificacion.morosos.recordatorio.pago.titulo" , null, LocaleConst.LOCALE_MX) );
		notificacionDto.setUsuarioCorreo( inmueble.getAdminBiCorreo() );
		
		LocalDateTime fechavencimientoPago = pago.getFechaCreacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays( DIAS_PARA_VENCIMIENTO_PAGO );
		LocalDateTime fechaActual =  LocalDateTime.now();
		
		Long diasAtraso = fechavencimientoPago.until(fechaActual, ChronoUnit.DAYS );
		
		StringBuffer notificacionDesc = new StringBuffer();
		notificacionDesc.append(",\n");
		notificacionDesc.append( messages.getMessage("notificacion.pago.concepto" , null, LocaleConst.LOCALE_MX) ).append(ComunConst.CADENA_ESPACIO).append( pago.getConcepto() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.pago.monto" , null, LocaleConst.LOCALE_MX) ).append(ComunConst.CADENA_ESPACIO).append( pago.getMonto() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.morosos.recordatorio.pago.atraso.dias" , null, LocaleConst.LOCALE_MX ) ).append(ComunConst.CADENA_ESPACIO).append( diasAtraso );
		
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
		notificacionDto.setTitulo( messages.getMessage("notificacion.pago.realizado.titulo" , null, LocaleConst.LOCALE_MX ) );
		
		StringBuffer notificacionDesc = new StringBuffer(  messages.getMessage("notificacion.pago.realizado.descripcion" , null, LocaleConst.LOCALE_MX )  );
		notificacionDesc.append("\n");
		notificacionDesc.append( messages.getMessage("notificacion.pago.socio" , null, LocaleConst.LOCALE_MX ) ).append(ComunConst.CADENA_ESPACIO).append( pagoDto.getUsuarioNombre() );
		notificacionDesc.append(",\n");
		notificacionDesc.append( messages.getMessage("notificacion.pago.concepto" , null, LocaleConst.LOCALE_MX ) ).append(ComunConst.CADENA_ESPACIO).append( pagoDto.getConcepto() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.pago.monto" , null, LocaleConst.LOCALE_MX ) ).append(ComunConst.CADENA_ESPACIO).append( pagoDto.getMonto() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.pago.tipopago" , null, LocaleConst.LOCALE_MX ) ).append(ComunConst.CADENA_ESPACIO).append( pagoDto.getTipoPagoDescripction() );
		
		notificacionDto.setDescripcion( notificacionDesc.toString() );
		
		notificacionService.notificarUsuario(notificacionDto);
    }

}
