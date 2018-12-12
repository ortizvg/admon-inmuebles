package mx.com.admoninmuebles.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import mx.com.admoninmuebles.dto.MensajeContactoDto;

public class ContactoTelefonosValidator implements ConstraintValidator<ContactoTelefonos, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		final MensajeContactoDto mensajeContactoDto = (MensajeContactoDto) value;
        return (mensajeContactoDto.getTelefono() != null && !mensajeContactoDto.getTelefono().trim().isEmpty()) || (mensajeContactoDto.getTelefonoAlternativo() != null && !mensajeContactoDto.getTelefonoAlternativo().trim().isEmpty());
	}

}
