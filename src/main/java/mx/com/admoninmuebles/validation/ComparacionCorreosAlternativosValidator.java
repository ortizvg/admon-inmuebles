package mx.com.admoninmuebles.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import mx.com.admoninmuebles.constant.ComunConst;
import mx.com.admoninmuebles.dto.UsuarioDto;

public class ComparacionCorreosAlternativosValidator  implements ConstraintValidator<ComparacionCorreosAlternativos, Object>{
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		final UsuarioDto usuarioDto = (UsuarioDto) value;
			String correoAlternativo1 = usuarioDto.getCorreoAlternativo1();
			String correoAlternativo2 = usuarioDto.getCorreoAlternativo2();
			if( correoAlternativo1 != null && correoAlternativo2 != null && 
					correoAlternativo1.trim() != ComunConst.CADENA_VACIA && correoAlternativo2.trim() != ComunConst.CADENA_VACIA) {
				return !correoAlternativo1.equalsIgnoreCase(correoAlternativo2);
				
			}
			
			return Boolean.TRUE;
		        
	}

}
