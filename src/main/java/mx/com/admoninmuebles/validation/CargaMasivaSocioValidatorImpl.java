package mx.com.admoninmuebles.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import mx.com.admoninmuebles.constant.LocaleConst;
import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.ErrorDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.SocioCargaMasivaDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.repository.InmuebleRepository;
import mx.com.admoninmuebles.persistence.repository.RolRepository;
import mx.com.admoninmuebles.persistence.repository.TipoSocioRepository;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.InmuebleService;

@Component
public class CargaMasivaSocioValidatorImpl implements CargaMasivaSocioValidator {



	@Autowired
	private InmuebleRepository inmuebleRepository;
	
	@Autowired
	private InmuebleService inmuebleService;

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
	
	private Collection<InmuebleDto> inmuebles;

	@Override
	public List<ErrorDto> validarCargaMasiva(List<UsuarioDto> listaSocios) {
		
		List<ErrorDto> listaErrores = new ArrayList<ErrorDto>();

		if (listaSocios == null || listaSocios.isEmpty()) {
			throw new BusinessException( messages.getMessage("socios.cargamasiva.validacion.lista.vacia", null, LocaleConst.LOCALE_MX) );
		}
		
		cargarInmueblesPorAdministradorLogueado();

		listaSocios.parallelStream().forEach(socio -> {
			try {
				SocioCargaMasivaDto socioCargaMasivaDto = modelMapper.map(socio, SocioCargaMasivaDto.class);
				validarSocio( socioCargaMasivaDto );
			} catch( Exception e) {
				ErrorDto error = new ErrorDto();
				error.setId(socio.getId());
				error.setMessage(e.getMessage());
				listaErrores.add(error);
			}
		});
		
		return listaErrores.stream()
				  .sorted(Comparator.comparing(ErrorDto::getId))
				  .collect(Collectors.toList());
	}
	
	private void cargarInmueblesPorAdministradorLogueado() {
		Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
		if( SecurityUtils.isCurrentUserInRole( RolConst.ROLE_ADMIN_BI ) ) {
			inmuebles = inmuebleService.findByAdminBiId( usuarioLogueadoId );
		} else if ( SecurityUtils.isCurrentUserInRole( RolConst.ROLE_ADMIN_ZONA ) ) {
			inmuebles = inmuebleService.findByAdminZonaId( usuarioLogueadoId );
		}
	}

	private void validarSocio(SocioCargaMasivaDto socioCargaMasivaDto) {
		validarCampos( socioCargaMasivaDto );
		existeUsername( socioCargaMasivaDto );
		validarInmueble( socioCargaMasivaDto );
		existeTipoSocio( socioCargaMasivaDto );
		existeRol( socioCargaMasivaDto );
	}

	private void validarCampos(SocioCargaMasivaDto socioCargaMasivaDto) {
		Set<ConstraintViolation<SocioCargaMasivaDto>> violations = validator.validate(socioCargaMasivaDto);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}

	private void validarInmueble(SocioCargaMasivaDto socioCargaMasivaDto) {
		boolean existeInmueble = inmuebleRepository.existsById( socioCargaMasivaDto.getInmuebleId() ); 
		if( !existeInmueble ) {
			throw new BusinessException (messages.getMessage("socios.cargamasiva.validacion.inmueble.no.existe", null, LocaleConst.LOCALE_MX) );
		}
		
		if( SecurityUtils.isCurrentUserInRole( RolConst.ROLE_ADMIN_BI ) ||  SecurityUtils.isCurrentUserInRole( RolConst.ROLE_ADMIN_ZONA ) ) {
			
			if( inmuebles == null ) {
				throw new BusinessException (messages.getMessage("socios.cargamasiva.validacion.inmueble.sin.inmuebles", null, LocaleConst.LOCALE_MX) );
			}
			
			boolean permisoParaInmueble = inmuebles.stream().anyMatch( inmueble -> inmueble.getId() == socioCargaMasivaDto.getInmuebleId() );
			
			if( !permisoParaInmueble ) {
				throw new BusinessException (messages.getMessage("socios.cargamasiva.validacion.inmueble.no.persmiso", null, LocaleConst.LOCALE_MX) );
			}
		}
		
	}
	
	private void existeTipoSocio(SocioCargaMasivaDto socioCargaMasivaDto) {
		boolean existeTipoSocio =  tipoSocioRepository.existsById(socioCargaMasivaDto.getTipoSocioId());
		if( !existeTipoSocio ) {
			throw new BusinessException (messages.getMessage("socios.cargamasiva.validacion.tiposocio.no.existe", null, LocaleConst.LOCALE_MX) );
		}
	}

	private void existeUsername(SocioCargaMasivaDto socioCargaMasivaDto) {
		 Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername( socioCargaMasivaDto.getUsername() );
	     if( usuarioOptional.isPresent() ) {
	    	 throw new BusinessException (messages.getMessage("socios.cargamasiva.validacion.usuario.ya.existe", null, LocaleConst.LOCALE_MX) );
	     }
	}
	
	private void existeRol(SocioCargaMasivaDto socioCargaMasivaDto) {
		boolean existeRol =  rolRepository.existsById( socioCargaMasivaDto.getRolSeleccionado() );
		if( !existeRol ) {
			throw new BusinessException (messages.getMessage("socios.cargamasiva.validacion.rol.no.existe", null, LocaleConst.LOCALE_MX) );
		}
	}

}
