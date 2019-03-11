package mx.com.admoninmuebles.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.constant.LocaleConst;
import mx.com.admoninmuebles.dto.AreaComunDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.NotificacionDto;
import mx.com.admoninmuebles.dto.ReservacionDto;
import mx.com.admoninmuebles.dto.SocioDto;

@Service
public class NotificacionReservacionServiceImpl implements NotificacionReservacionService {
	
	private static final Long DIAS_EXPOSICION_NOTIFICACION = 5L;
	
	@Autowired
	private NotificacionService notificacionService;
	
	@Autowired
	private InmuebleService inmuebleService;
	
	@Autowired
	private SocioService socioService;
	
	@Autowired
	private AreaComunService areaComunService;
	
    @Autowired
    private MessageSource messages;
    

    @Transactional
	@Override
	public void notificarReservacion(ReservacionDto reservacionDto) {
		InmuebleDto inmueble = inmuebleService.findBySocioId( reservacionDto.getSocioId() );
		notificarReservacionAdministradorBi(reservacionDto, inmueble.getAdminBiId(), inmueble.getAdminBiCorreo());

	}

	private void notificarReservacionAdministradorBi(ReservacionDto reservacionDto, Long idAdminBiANotificar, String correo) {
		
		SocioDto socioDto = socioService.buscarSocioPorId( reservacionDto.getSocioId() );
		AreaComunDto areaComunDto = areaComunService.findById( reservacionDto.getAreaComunId() );
		
		
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setUsuarioCorreo(correo);
		notificacionDto.setFechaExposicionInicial(LocalDate.now());
		notificacionDto.setFechaExposicionFinal(LocalDate.now().plusDays(DIAS_EXPOSICION_NOTIFICACION));
		notificacionDto.setUsuarioId(idAdminBiANotificar);
		notificacionDto.setTitulo(messages.getMessage("notificacion.reservacion.realizado.titulo", null, LocaleConst.LOCALE_MX));

		StringBuffer notificacionDesc = new StringBuffer(messages.getMessage("notificacion.reservacion.realizado.descripcion", null, LocaleConst.LOCALE_MX));
		notificacionDesc.append("\n");
		notificacionDesc.append(messages.getMessage("notificacion.reservacion.socio", null, LocaleConst.LOCALE_MX)).append(" ").append( socioDto.getNombreCompleto() );
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.reservacion.fecha", null, LocaleConst.LOCALE_MX)).append(" ").append(reservacionDto.getStart());
		notificacionDesc.append(",\n");
		notificacionDesc.append(messages.getMessage("notificacion.reservacion.area", null, LocaleConst.LOCALE_MX)).append(" ").append( areaComunDto.getNombre() );

		notificacionDto.setDescripcion(notificacionDesc.toString());

		notificacionService.notificarUsuario(notificacionDto);
	}

}
