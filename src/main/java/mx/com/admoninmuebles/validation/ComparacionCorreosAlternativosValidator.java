package mx.com.admoninmuebles.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.com.admoninmuebles.constant.ComunConst;
import mx.com.admoninmuebles.controller.SocioController;
import mx.com.admoninmuebles.dto.UsuarioDto;

public class ComparacionCorreosAlternativosValidator  implements ConstraintValidator<ComparacionCorreosAlternativos, Object>{
	
	Logger logger = LoggerFactory.getLogger(SocioController.class);
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		logger.info("VALIDANDO CORREOS ALTERNATIVOS");
		final UsuarioDto usuarioDto = (UsuarioDto) value;
			String correoAlternativo1 = usuarioDto.getCorreoAlternativo1();
			String correoAlternativo2 = usuarioDto.getCorreoAlternativo2();
			logger.info("CORREO1: " + correoAlternativo1);
			logger.info("CORREO2: " + correoAlternativo2);
			if( correoAlternativo1 != null && correoAlternativo2 != null && 
					correoAlternativo1.trim() != ComunConst.CADENA_VACIA && correoAlternativo2.trim() != ComunConst.CADENA_VACIA) {
				logger.info("REGRESO: " + correoAlternativo1.equalsIgnoreCase(correoAlternativo2));
				return !correoAlternativo1.equalsIgnoreCase(correoAlternativo2);
				
			}
			
			return Boolean.TRUE;
		        
	}

}
