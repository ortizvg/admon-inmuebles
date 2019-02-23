package mx.com.admoninmuebles.validation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import mx.com.admoninmuebles.constant.LocaleConst;
import mx.com.admoninmuebles.dto.SocioCargaMasivaDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.repository.InmuebleRepository;
import mx.com.admoninmuebles.persistence.repository.RolRepository;
import mx.com.admoninmuebles.persistence.repository.TipoSocioRepository;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;

@Component
public class CargaMasivaSocioValidatorImpl implements CargaMasivaSocioValidator {



	@Autowired
	private InmuebleRepository inmuebleRepository;

	@Autowired
	private TipoSocioRepository tipoSocioRepository;

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private Validator validator;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MessageSource messages;

	@Override
	public void validarCargaMasiva(List<UsuarioDto> listaSocios) {

		if (listaSocios == null || listaSocios.isEmpty()) {
			throw new BusinessException( messages.getMessage("socio.cargamasiva.validacion.lista.vacia", null, LocaleConst.LOCALE_MX) );
		}

		listaSocios.parallelStream().forEach(socio -> {
			SocioCargaMasivaDto socioCargaMasivaDto = modelMapper.map(socio, SocioCargaMasivaDto.class);
			validarSocio( socioCargaMasivaDto );
		});
	}

	private void validarSocio(SocioCargaMasivaDto socioCargaMasivaDto) {
		validarCampos( socioCargaMasivaDto );
		existeUsername( socioCargaMasivaDto );
		existeInmueble( socioCargaMasivaDto );
		existeTipoSocio( socioCargaMasivaDto );
		existeRol( socioCargaMasivaDto );
	}

	private void validarCampos(SocioCargaMasivaDto socioCargaMasivaDto) {
		Set<ConstraintViolation<SocioCargaMasivaDto>> violations = validator.validate(socioCargaMasivaDto);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}

	private void existeInmueble(SocioCargaMasivaDto socioCargaMasivaDto) {
		boolean existeInmueble = inmuebleRepository.existsById( socioCargaMasivaDto.getInmuebleId() ); 
		if( !existeInmueble ) {
			throw new BusinessException (messages.getMessage("socio.cargamasiva.validacion.inmueble.no.existe", null, LocaleConst.LOCALE_MX) );
		}
	}

	private void existeTipoSocio(SocioCargaMasivaDto socioCargaMasivaDto) {
		boolean existeTipoSocio =  tipoSocioRepository.existsById(socioCargaMasivaDto.getTipoSocioId());
		if( !existeTipoSocio ) {
			throw new BusinessException (messages.getMessage("socio.cargamasiva.validacion.tiposocio.no.existe", null, LocaleConst.LOCALE_MX) );
		}
	}

	private void existeUsername(SocioCargaMasivaDto socioCargaMasivaDto) {
		 Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername( socioCargaMasivaDto.getUsername() );
	     if( usuarioOptional.isPresent() ) {
	    	 throw new BusinessException (messages.getMessage("socio.cargamasiva.validacion.usuario.ya.existe", null, LocaleConst.LOCALE_MX) );
	     }
	}
	
	private void existeRol(SocioCargaMasivaDto socioCargaMasivaDto) {
		boolean existeRol =  rolRepository.existsById( socioCargaMasivaDto.getRolSeleccionado() );
		if( !existeRol ) {
			throw new BusinessException (messages.getMessage("socio.cargamasiva.validacion.rol.no.existe", null, LocaleConst.LOCALE_MX) );
		}
	}

}
