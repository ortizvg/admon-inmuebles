package mx.com.admoninmuebles.validation;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import mx.com.admoninmuebles.dto.NotificacionDto;

public class ComparacionFechasValidator  implements ConstraintValidator<ComparacionFechas, Object>{

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		final NotificacionDto notificacionDto = (NotificacionDto) value;
			LocalDate hoy = LocalDate.now();
			if(notificacionDto.getFechaExposicionInicial().isBefore(hoy) || notificacionDto.getFechaExposicionFinal().isBefore(hoy)){
				return false;
			}else if(notificacionDto.getFechaExposicionFinal().isBefore( notificacionDto.getFechaExposicionInicial())){
				return false;
			} else {
				return true;
			}
		        
	}

}
