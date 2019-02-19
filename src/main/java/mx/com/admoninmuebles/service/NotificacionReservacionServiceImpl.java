package mx.com.admoninmuebles.service;

import java.time.LocalDate;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.NotificacionDto;
import mx.com.admoninmuebles.dto.ReservacionDto;

@Service
public class NotificacionReservacionServiceImpl implements NotificacionReservacionService {
	
	private static final Long DIAS_EXPOSICION_NOTIFICACION = 5L;
	
	@Autowired
	private NotificacionService notificacionService;
	
	@Autowired
	private InmuebleService inmuebleService;
	
    @Autowired
    private MessageSource messages;
    

    @Transactional
	@Override
	public void notificarReservacion(ReservacionDto reservacionDto) {
		InmuebleDto inmueble = inmuebleService.findBySocioId( reservacionDto.getSocioId() );
		notificarReservacionAdministradorBi(reservacionDto, inmueble.getAdminBiId(), inmueble.getAdminBiCorreo());

	}

	private void notificarReservacionAdministradorBi(ReservacionDto reservacionDto, Long idAdminBiANotificar, String correo) {
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setUsuarioCorreo(correo);
		notificacionDto.setFechaExposicionInicial(LocalDate.now());
		notificacionDto.setFechaExposicionFinal(LocalDate.now().plusDays(DIAS_EXPOSICION_NOTIFICACION));
		notificacionDto.setUsuarioId(idAdminBiANotificar);
		notificacionDto.setTitulo(messages.getMessage("notificacion.reservacion.realizado.titulo", null, Locale.getDefault()));

		StringBuffer notificacionDesc = new StringBuffer(messages.getMessage("notificacion.reservacion.realizado.descripcion", null, Locale.getDefault()));
		notificacionDesc.append("\n");
		notificacionDesc.append(messages.getMessage("notificacion.reservacion.socio", null, Locale.getDefault())).append(" ").append(reservacionDto.getSocioNombre());
		notificacionDesc.append("\n");
		notificacionDesc.append(messages.getMessage("notificacion.reservacion.fecha", null, Locale.getDefault())).append(" ").append(reservacionDto.getStart());
		notificacionDesc.append("\n");
		notificacionDesc.append(messages.getMessage("notificacion.reservacion.area", null, Locale.getDefault())).append(" ").append(reservacionDto.getAreaComunNombre());

		notificacionDto.setDescripcion(notificacionDesc.toString());

		notificacionService.notificarUsuario(notificacionDto);
	}

}
