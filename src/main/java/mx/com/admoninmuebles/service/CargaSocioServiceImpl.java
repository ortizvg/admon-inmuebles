package mx.com.admoninmuebles.service;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.ComunConst;
import mx.com.admoninmuebles.constant.LocaleConst;
import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.constant.SociosConst;
import mx.com.admoninmuebles.dto.CargaSocioDto;
import mx.com.admoninmuebles.dto.ErrorDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Inmueble;
import mx.com.admoninmuebles.persistence.model.Rol;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.repository.InmuebleRepository;
import mx.com.admoninmuebles.persistence.repository.RolRepository;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;
import mx.com.admoninmuebles.validation.CargaMasivaSocioValidator;

@Service
public class CargaSocioServiceImpl implements CargaSocioService {

	Logger logger = LoggerFactory.getLogger(CargaSocioServiceImpl.class);
	
	@Autowired
	private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
	
	@Autowired
    private ModelMapper modelMapper;
	
	@Autowired
	private InmuebleRepository inmuebleRepository;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CorreoUsuarioService correoUsuarioService;
    
    @Autowired
    private CargaMasivaSocioValidator cargaMasivaSocioValidator;
    
	@Autowired
	private MessageSource messages;

	/**
	 * Lee archivo para carga de usuarios en 
	 * base de datos.
	 */
	@Override
	public CargaSocioDto validaCSVSocios(BufferedReader br) {
		String line = null;
		CargaSocioDto socio = new CargaSocioDto();
		long contadorLinea = 0;
		List<ErrorDto> listaErroresValidacion = null;
		Rol rolSocio = rolRepository.findByNombre(RolConst.ROLE_SOCIO_BI).get();
		try {
			while((line = br.readLine()) != null) {
				if( !line.isEmpty() ) {
					contadorLinea++;
					String[] arrayStr = line.split(ComunConst.COMA);
					agregarSocio(arrayStr, contadorLinea, socio, rolSocio.getId() );
				}
			}
			
			if( socio.getListaErrores().isEmpty() ) {
				listaErroresValidacion =  cargaMasivaSocioValidator.validarCargaMasiva( socio.getListaSocios() );
				if( listaErroresValidacion != null && !listaErroresValidacion.isEmpty() ) {
					socio.setListaErrores(listaErroresValidacion);
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ErrorDto error = new ErrorDto();
			error.setId(contadorLinea);
			error.setMessage(e.getMessage());
			socio.getListaErrores().add( error );
		} 
		if(socio.getListaErrores() == null || socio.getListaErrores().isEmpty()) {
			List<UsuarioDto> listaSociosCreados = guardaSocioMasivo(socio.getListaSocios());
			socio.setListaSocios(listaSociosCreados);
		}
		return socio;
	}
	

	/**
	 * Llena el objeto de socios y/o errores encontrados
	 * en el procesamiento, si no hay errores guarda a los
	 * usuarios.
	 * @param arrayStr
	 * @param contador
	 * @param cargaSocio
	 * @return
	 */
	private CargaSocioDto agregarSocio(String[] arrayStr, long contador, CargaSocioDto cargaSocio, long rolId) {
		UsuarioDto socio = new UsuarioDto();
		List<UsuarioDto> listaSocios = null;
		try {
			socio.setId(contador);
			socio.setUsername(arrayStr[SociosConst.USER_NAME]);
//			socio = validaUserName(socio);
			socio.setCorreo(arrayStr[SociosConst.CORREO]);
			socio.setNombre(arrayStr[SociosConst.NOMBRE]);
			socio.setApellidoPaterno(arrayStr[SociosConst.APELLIDO_PATERNO]);
			socio.setApellidoMaterno(arrayStr[SociosConst.APELLIDO_MATERNO]);
			try {
				socio.setCoutaMensualPagoSocio(new BigDecimal(arrayStr[SociosConst.CUOTA_MENSUAL_PAGO_SOCIO]));
			} catch ( NumberFormatException e ) {
				ErrorDto errorDto = new ErrorDto();
				errorDto.setId( contador );
				errorDto.setMessage( messages.getMessage("socios.cargamasiva.validacion.valor.cuota", null, LocaleConst.LOCALE_MX) );
				cargaSocio.getListaErrores().add( errorDto );
			}
			try {
				socio.setTipoSocioId( Long.valueOf(arrayStr[SociosConst.TIPO_SOCIO]) );
			} catch ( NumberFormatException e ) {
				ErrorDto errorDto = new ErrorDto();
				errorDto.setId( contador );
				errorDto.setMessage( messages.getMessage("socios.cargamasiva.validacion.valor.tiposocio", null, LocaleConst.LOCALE_MX) );
				cargaSocio.getListaErrores().add( errorDto );
			}
			try {
				socio.setInmuebleId(Long.valueOf(arrayStr[SociosConst.INMUEBLE_ID]));
			} catch ( NumberFormatException e ) {
				ErrorDto errorDto = new ErrorDto();
				errorDto.setId( contador );
				errorDto.setMessage( messages.getMessage("socios.cargamasiva.validacion.valor.inmueble", null, LocaleConst.LOCALE_MX) );
				cargaSocio.getListaErrores().add( errorDto );
			}
			socio.setCorreoAlternativo1(arrayStr[SociosConst.CORREO_ALTERNATIVO_1]);
			socio.setCorreoAlternativo2(arrayStr[SociosConst.CORREO_ALTERNATIVO_2]);
//			socio.setRolSeleccionado(Long.valueOf(arrayStr[SociosConst.ROL_ID]));
			socio.setRolSeleccionado( rolId );
			socio.setDatosDomicilio(arrayStr[SociosConst.DATOS_DOMICILIO]);
			if(cargaSocio.getListaSocios() == null || cargaSocio.getListaSocios().isEmpty()) {
				listaSocios = new ArrayList<UsuarioDto>();
				listaSocios.add(socio);
				cargaSocio.setListaSocios(listaSocios);
			}else {
				listaSocios = cargaSocio.getListaSocios();
				listaSocios.add(socio);
				cargaSocio.setListaSocios(listaSocios);
			}
			
		} catch (Exception e) {
			logger.error("Dato sin podeer leer en linea" + contador + e.getMessage() , e);
			ErrorDto errorDto = new ErrorDto();
			errorDto.setId( contador );
			errorDto.setMessage( e.getMessage() );
			cargaSocio.getListaErrores().add( errorDto );
		}
		return cargaSocio;
	}

	/**
	 * Valida si el nombre de usuario existe,
	 * si no existe le asigna como contrasenia
	 * el mismo nombre de usuario
	 * @param socio
	 * @return
	 */
	private UsuarioDto validaUserName(UsuarioDto socio) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(socio.getUsername());
        if (usuarioOptional.isPresent()) {
            throw new BusinessException("El usuario ya existe");
        }
        socio.setContrasenia(passwordEncoder.encode(socio.getUsername()));
        return socio;
		
	}
	
	/**
	 * Guarda varios socios con sus
	 * respectivos roles
	 * @param listaSocios
	 */
	private List<UsuarioDto> guardaSocioMasivo(List<UsuarioDto> listaSocios) {
		List<UsuarioDto> listaSociosCreados = new ArrayList<>();
		for (UsuarioDto usuarioDto : listaSocios) {
	        Usuario usuario = modelMapper.map(usuarioDto, Usuario.class);

	        Collection<Rol> roles = new ArrayList<>();
	        roles.add(rolRepository.findById(usuarioDto.getRolSeleccionado()).get());
	        usuario.setRoles(roles);
	        Usuario usuarioCreado = usuarioRepository.save(usuario);
	        guardaInmuebleActualizaUsuario(usuarioCreado, usuarioDto.getInmuebleId());
	        UsuarioDto usuarioCreadoDto =  modelMapper.map(usuarioCreado, UsuarioDto.class);
	        listaSociosCreados.add( usuarioCreadoDto );
		}
		
		return listaSociosCreados;
	}
	
//	private List<UsuarioDto> guardaSocioMasivo(List<UsuarioDto> listaSocios) {
//		Collection<Rol> roles = new ArrayList<>();
//		roles.add(rolRepository.findByNombre(RolConst.ROLE_SOCIO_BI).get());
//		
//		List<Usuario> socios = listaSocios.parallelStream().map( socio -> {
//			Usuario usuario = modelMapper.map(socio, Usuario.class);
//			usuario.setRoles(roles);
//			return usuario;
//		}).collect(Collectors.toList());
//		
//		Iterable<Usuario> sociosCreados = usuarioRepository.saveAll( socios );
//		
//		return StreamSupport.stream(sociosCreados.spliterator(), false)
//				.map(socio -> modelMapper.map(socio, UsuarioDto.class))
//				.collect(Collectors.toList());
//		
//	}
	
	

	/**
	 * Se recupera el inmueble, se actualiza
	 * el usuario con la referencia de pagos
	 * y se actualiza el inmueble agregando 
	 * el usuario creado
	 * @param usuarioCreado
	 * @param inmuebleId
	 */
	private void guardaInmuebleActualizaUsuario(Usuario usuarioCreado, Long inmuebleId) {
		Inmueble inmueble = inmuebleRepository.findById(inmuebleId).get();
		usuarioCreado.setReferenciaPagoSocio(usuarioCreado.getId() + "-" + inmueble.getDatosAdicionales().getNumeroCuenta());
		usuarioCreado.setCuentaPagoSocio( inmueble.getDatosAdicionales().getNumeroCuenta() );
		inmueble.addSocio(usuarioRepository.save(usuarioCreado));
		inmuebleRepository.save(inmueble);
		
	}
	
	@Async
	@Override
	public void enviarCorreoMasivo(List<UsuarioDto> listaSocios, final String urlContext) {
		for (UsuarioDto usuarioDto : listaSocios) {
			correoUsuarioService.enviarActivacion(usuarioDto, urlContext);
		}
	}
	
	public static void main(String[] args) {
		for(int i = 1; i<=100; i++) {
			System.out.println("paco10" + i + ",ffcojaviercarrillo@gmail.com,Paco,Carrillo,Medina,100.14,correoalternativo1@gmail.com,correoalternativo2@gmail.com,31,1,Mi domicilio,45");
		}
	}

}
