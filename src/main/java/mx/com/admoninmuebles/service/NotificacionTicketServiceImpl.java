package mx.com.admoninmuebles.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.ComunConst;
import mx.com.admoninmuebles.constant.LocaleConst;
import mx.com.admoninmuebles.dto.CambioTicketDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.NotificacionDto;
import mx.com.admoninmuebles.dto.TicketDto;
import mx.com.admoninmuebles.dto.TipoTicketDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.security.SecurityUtils;

@Service
public class NotificacionTicketServiceImpl implements NotificacionTicketService {
	
	Logger logger = LoggerFactory.getLogger(NotificacionTicketServiceImpl.class);
	
	private static final Long DIAS_EXPOSICION_NOTIFICACION = 5L;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private NotificacionService notificacionService;
	
    @Autowired
    private UsuarioService usuarioService;
    
	@Autowired
	private InmuebleService inmuebleService;
	
	@Autowired
	private TipoTicketService tipoTicketService;
    
    @Autowired
    private MessageSource messages;
	
	@Override
	public void notificarAsignacionTicket(TicketDto ticketDto) {
		
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		UsuarioDto usuarioAsignador = usuarioService.findById( usuarioLogueadoId );
		UsuarioDto usuarioAsignado = usuarioService.findById( ticketDto.getUsuarioAsignadoId() );
		UsuarioDto usuarioGenerador = usuarioService.findById( ticketDto.getUsuarioCreadorId() );
		InmuebleDto inmueleDto = inmuebleService.findBySocioId( usuarioGenerador.getId() );
		TipoTicketDto tipoTicketDto = tipoTicketService.findById( ticketDto.getTipoTicketId() );
		
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setFechaExposicionInicial( LocalDate.now() );
		notificacionDto.setFechaExposicionFinal( LocalDate.now().plusDays( DIAS_EXPOSICION_NOTIFICACION ) );
		notificacionDto.setUsuarioId( usuarioAsignado.getId() );
		notificacionDto.setUsuarioCorreo(usuarioAsignado.getCorreo());
		
		StringBuffer titulo = new StringBuffer(   messages.getMessage("notificacion.ticket.asignacion.titulo" , null, LocaleConst.LOCALE_MX )  );
		titulo.append( ComunConst.CADENA_ESPACIO );
		titulo.append( usuarioAsignador.getNombreCompleto() );
		
		notificacionDto.setTitulo( titulo.toString() );
		
		StringBuffer notificacionDesc = new StringBuffer();
		notificacionDesc.append( messages.getMessage("notificacion.ticket.asignacion.ticket.id" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( ticketDto.getId() );
		notificacionDesc.append(",\n");
		notificacionDesc.append( messages.getMessage("notificacion.ticket.asignacion.tipo.ticket" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( tipoTicketDto.getNombre() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.ticket.asignacion.generado.por" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( usuarioGenerador.getNombreCompleto() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.ticket.asignacion.inmueble" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( inmueleDto.getNombre() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.ticket.asignacion.mensaje" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( ticketDto.getDescripcion() );
		
		notificacionDto.setDescripcion( notificacionDesc.toString() );
		
		
		notificacionService.notificarUsuario(notificacionDto);
		
	}

	@Override
	public void notificarCierreTicket(TicketDto ticketDto) {
		
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		UsuarioDto usuarioFinalizador = usuarioService.findById( usuarioLogueadoId );
		TipoTicketDto tipoTicketDto = tipoTicketService.findById( ticketDto.getTipoTicketId() );
		UsuarioDto usuarioCreador = usuarioService.findById( ticketDto.getUsuarioCreadorId()  );
		UsuarioDto usuarioAsignado = usuarioService.findById( ticketDto.getUsuarioAsignadoId() );
		InmuebleDto inmueleDto = inmuebleService.findBySocioId( usuarioCreador.getId() );
		UsuarioDto administradorBi = usuarioService.findById( inmueleDto.getAdminBiId() );
		
		notificarCierreTicket(ticketDto, usuarioFinalizador, tipoTicketDto, usuarioCreador);
		notificarCierreTicket(ticketDto, usuarioFinalizador, tipoTicketDto, usuarioAsignado);
		
		if( usuarioAsignado.getId() != administradorBi.getId() ) {
			notificarCierreTicket( ticketDto, usuarioFinalizador, tipoTicketDto, administradorBi);
		}
		
	}
	
	private void notificarCierreTicket(TicketDto ticketDto, UsuarioDto usuarioFinalizador, TipoTicketDto tipoTicketDto, UsuarioDto destinatario) {
		
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setFechaExposicionInicial( LocalDate.now() );
		notificacionDto.setFechaExposicionFinal( LocalDate.now().plusDays( DIAS_EXPOSICION_NOTIFICACION ) );
		notificacionDto.setUsuarioId( destinatario.getId() );
		notificacionDto.setUsuarioCorreo( destinatario.getCorreo() );
		
		StringBuffer titulo = new StringBuffer(   messages.getMessage("notificacion.ticket.cierre.titulo" , null, LocaleConst.LOCALE_MX )  );
		titulo.append( ComunConst.CADENA_ESPACIO );
		titulo.append( usuarioFinalizador.getNombreCompleto() );
		
		notificacionDto.setTitulo( titulo.toString() );
		
		StringBuffer notificacionDesc = new StringBuffer();
		notificacionDesc.append( messages.getMessage("notificacion.ticket.cierre.ticket.id" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( ticketDto.getId() );
		notificacionDesc.append(",\n");
		notificacionDesc.append( messages.getMessage("notificacion.ticket.cierre.tipo.ticket" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( tipoTicketDto.getNombre() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.ticket.cierre.mensaje" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( ticketDto.getDescripcion() );
		
		notificacionDto.setDescripcion( notificacionDesc.toString() );
		
		
		notificacionService.notificarUsuario(notificacionDto);
		
	}

	@Override
	public void notificarCreacionTicket(TicketDto ticketDto) {
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		UsuarioDto usuarioCreador = usuarioService.findById( usuarioLogueadoId);
		InmuebleDto inmueleDto = inmuebleService.findBySocioId( usuarioLogueadoId );
		TipoTicketDto tipoTicketDto = tipoTicketService.findById( ticketDto.getTipoTicketId() );
		
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setFechaExposicionInicial( LocalDate.now() );
		notificacionDto.setFechaExposicionFinal( LocalDate.now().plusDays( DIAS_EXPOSICION_NOTIFICACION ) );
		notificacionDto.setUsuarioId( inmueleDto.getAdminBiId() );
		notificacionDto.setUsuarioCorreo( inmueleDto.getAdminBiCorreo() );
		
		StringBuffer titulo = new StringBuffer(   messages.getMessage("notificacion.ticket.creacion.titulo" , null, LocaleConst.LOCALE_MX )  );
		titulo.append( ComunConst.CADENA_ESPACIO );
		titulo.append( usuarioCreador.getNombreCompleto() );
		
		notificacionDto.setTitulo( titulo.toString() );
		
		StringBuffer notificacionDesc = new StringBuffer();
		notificacionDesc.append( messages.getMessage("notificacion.ticket.creacion.ticket.id" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( ticketDto.getId() );
		notificacionDesc.append(",\n");
		notificacionDesc.append( messages.getMessage("notificacion.ticket.creacion.tipo.ticket" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( tipoTicketDto.getNombre() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.ticket.creacion.inmueble" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( inmueleDto.getNombre() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.ticket.creacion.mensaje" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( ticketDto.getDescripcion() );
		
		notificacionDto.setDescripcion( notificacionDesc.toString() );
		
		
		notificacionService.notificarUsuario(notificacionDto);
		
	}

	@Override
	public void notificarComentarioTicket(CambioTicketDto cambioTicketDto) {
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		TicketDto ticketDto = ticketService.findById( cambioTicketDto.getTicketId() );
		UsuarioDto usuarioModificador = usuarioService.findById( usuarioLogueadoId );
		UsuarioDto usuarioCreador = usuarioService.findById( ticketDto.getUsuarioCreadorId() );
		UsuarioDto usuarioAsignado = usuarioService.findById( ticketDto.getUsuarioAsignadoId() );
		InmuebleDto inmueleDto = inmuebleService.findBySocioId( usuarioCreador.getId() );
		UsuarioDto administradorBi = usuarioService.findById( inmueleDto.getAdminBiId() );
		
		notificarComentario( cambioTicketDto, usuarioModificador, usuarioCreador);
		notificarComentario( cambioTicketDto, usuarioModificador, usuarioAsignado);
		
		if( usuarioAsignado.getId() != administradorBi.getId() ) {
			notificarComentario( cambioTicketDto, usuarioModificador, administradorBi);
		}
		
	}
	
	private void notificarComentario(CambioTicketDto cambioTicketDto, UsuarioDto usuarioModificador,  UsuarioDto destinatario) {
		
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setFechaExposicionInicial( LocalDate.now() );
		notificacionDto.setFechaExposicionFinal( LocalDate.now().plusDays( DIAS_EXPOSICION_NOTIFICACION ) );
		notificacionDto.setUsuarioId( destinatario.getId() );
		notificacionDto.setUsuarioCorreo( destinatario.getCorreo() );
		
		StringBuffer titulo = new StringBuffer(   messages.getMessage("notificacion.ticket.cambio.titulo" , null, LocaleConst.LOCALE_MX )  );
		titulo.append( ComunConst.CADENA_ESPACIO );
		titulo.append( usuarioModificador.getNombreCompleto() );
		
		notificacionDto.setTitulo( titulo.toString() );
		
		StringBuffer notificacionDesc = new StringBuffer();
		notificacionDesc.append( messages.getMessage("notificacion.ticket.cambio.ticket.id" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( cambioTicketDto.getTicketId() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.ticket.cambio.comentario" , null, LocaleConst.LOCALE_MX) ).append( ComunConst.CADENA_ESPACIO ).append( cambioTicketDto.getComentario() );
		
		notificacionDto.setDescripcion( notificacionDesc.toString() );
		
		
		notificacionService.notificarUsuario(notificacionDto);
		
	}

}
