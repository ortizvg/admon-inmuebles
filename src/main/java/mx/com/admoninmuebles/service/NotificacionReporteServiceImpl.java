package mx.com.admoninmuebles.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.LocaleConst;
import mx.com.admoninmuebles.dto.ComunicadoDto;
import mx.com.admoninmuebles.dto.NotificacionDto;

@Service
public class NotificacionReporteServiceImpl implements NotificacionReporteService{
	
	private static final Long DIAS_EXPOSICION_NOTIFICACION = 5L;
	
	@Autowired
	private NotificacionService notificacionService;
	
    @Autowired
    private MessageSource messages;
    

	@Override
	public void notificarComunicado(ComunicadoDto comunicadoDto) {
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setFechaExposicionInicial(LocalDate.now());
		notificacionDto.setFechaExposicionFinal(LocalDate.now().plusDays(DIAS_EXPOSICION_NOTIFICACION));
		notificacionDto.setTitulo(messages.getMessage("notificacion.comunicado.nuevo.titulo", null, LocaleConst.LOCALE_MX ));
		notificacionDto.setInmuebleId(comunicadoDto.getInmuebleId());
		
		StringBuffer notificacionDesc = new StringBuffer(messages.getMessage("notificacion.comunicado.nuevo.descripcion", null, LocaleConst.LOCALE_MX ));
//		notificacionDesc.append("\n");
//		notificacionDesc.append(messages.getMessage("notificacion.comunicado.inmueble", null, LocaleConst.LOCALE_MX)).append(" ").append(comunicadoDto.getInmuebleNombre());
		notificacionDesc.append("\n");
		notificacionDesc.append(messages.getMessage("notificacion.comunicado.descripcion", null, LocaleConst.LOCALE_MX )).append(" ").append( comunicadoDto.getDescripcion() );

		notificacionDto.setDescripcion(notificacionDesc.toString());

		notificacionService.notificarInmueble(notificacionDto);
	}

}
