package mx.com.admoninmuebles.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import mx.com.admoninmuebles.dto.ContraseniaDto;



public class ContraseniaConfirmacionValidator  implements ConstraintValidator<ContraseniaConfirmacion, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		final ContraseniaDto contraseniaDto = (ContraseniaDto) value;
        return contraseniaDto.getContrasenia().equals(contraseniaDto.getConfirmacionContrasenia());
	}

}
