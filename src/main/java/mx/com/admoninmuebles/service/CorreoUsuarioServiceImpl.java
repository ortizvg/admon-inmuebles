package mx.com.admoninmuebles.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import mx.com.admoninmuebles.constant.LocaleConst;
import mx.com.admoninmuebles.constant.PlantillaCorreoConst;
import mx.com.admoninmuebles.dto.CorreoDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.persistence.model.Rol;
import mx.com.admoninmuebles.persistence.repository.RolRepository;

@Service
public class CorreoUsuarioServiceImpl implements CorreoUsuarioService {
	
	private final static String URL_PRINCIPAL_GESCO = "gesco.url.principal";
	private final static String URL_ACTIVACION_CUENTA = "/usuarios/activar/";
	private final static String URL_RECUPERACION_CONTRASENIA= "/usuarios/recuperar-contrasenia/";
	private final static String PARAMETRO_CORREO_URL_ACTIVACION = "urlActivacion";
	private final static String PARAMETRO_CORREO_URL_RECUPERACION_CONTRASENIA = "urlRecuperacionContrasenia";
	private final static String PROPIEDAD_CORREO_USUARIOS_DE = "usuario.cuenta.correo.de";
	private final static String PROPIEDAD_CUENTA_ACTIVACION_ASUNTO = "usuario.cuenta.activacion.asunto";
	private final static String PROPIEDAD_CUENTA_RECUPERACION_CONTRASENIA_ASUNTO = "usuario.cuenta.recuperacion.contrasenia.asunto";
	
	@Autowired
    private RecuperacionContraseniaService recuperacionContraseniaService;
	
	@Autowired
    private ActivacionUsuarioService activacionUsuarioService;
	
	@Autowired
	private CorreoService correoService;
	
	@Autowired
	private RolRepository rolRepository;
	
    @Autowired
    private Environment env;
    
    @Autowired
    private MessageSource messages;

	@Override
	public void enviarActivacion( final UsuarioDto usuarioDto, final String urlContext ) {
		Rol rol = rolRepository.findByUserId( usuarioDto.getId() ).get();
		String urlActiacion = getUrlActivacion( usuarioDto, urlContext );
		
		Context datosPlantilla = new Context();
		datosPlantilla.setVariable( PARAMETRO_CORREO_URL_ACTIVACION, urlActiacion );
		datosPlantilla.setVariable( "nombre", usuarioDto.getNombreCompleto() );
		datosPlantilla.setVariable( "username", usuarioDto.getUsername() );
		datosPlantilla.setVariable( "rol", rol.getDescripcion() );
		datosPlantilla.setVariable( "url", env.getProperty( URL_PRINCIPAL_GESCO ) );
		
		CorreoDto correoDto = new CorreoDto();
		correoDto.setDe( env.getProperty( PROPIEDAD_CORREO_USUARIOS_DE ) );
		correoDto.setAsunto(messages.getMessage(PROPIEDAD_CUENTA_ACTIVACION_ASUNTO , null, LocaleConst.LOCALE_MX ) );
		correoDto.setPlantilla( PlantillaCorreoConst.ACTIVACION_CUENTA );
		correoDto.setPara(usuarioDto.getCorreo());
		correoDto.setDatosPlantilla( datosPlantilla );
		
		correoService.enviarCorreo( correoDto );
	}

	@Override
	public void enviarRecuperacionContrasenia(final UsuarioDto usuarioDto, final String urlContext) {
		String urlRecuperacionContrasenia = getUrlRecuperacionContrasenia( usuarioDto, urlContext );
		
		Context datosPlantilla = new Context();
		datosPlantilla.setVariable( PARAMETRO_CORREO_URL_RECUPERACION_CONTRASENIA, urlRecuperacionContrasenia );
		datosPlantilla.setVariable( "nombre", usuarioDto.getUsername() );
		
		CorreoDto correoDto = new CorreoDto();
		correoDto.setDe( env.getProperty( PROPIEDAD_CORREO_USUARIOS_DE ) );
		correoDto.setAsunto(messages.getMessage(PROPIEDAD_CUENTA_RECUPERACION_CONTRASENIA_ASUNTO , null, LocaleConst.LOCALE_MX ) );
		correoDto.setPlantilla( PlantillaCorreoConst.RECUPERA_CONTRASENIA );
		correoDto.setPara(usuarioDto.getCorreo());
		correoDto.setDatosPlantilla( datosPlantilla );
		
		correoService.enviarCorreo( correoDto );

	}
	
	private String getUrlActivacion(UsuarioDto usuarioDto, String urlContext) {
		final String token = UUID.randomUUID().toString();
		activacionUsuarioService.guardarToken(usuarioDto, token);
		StringBuffer urlActivacion = new StringBuffer( urlContext )
				.append(URL_ACTIVACION_CUENTA)
				.append(token);
		
		return urlActivacion.toString();
	}
	
	private String getUrlRecuperacionContrasenia(UsuarioDto usuarioDto, String urlContext) {
		final String token = UUID.randomUUID().toString();
		recuperacionContraseniaService.guardarToken(usuarioDto, token);
		StringBuffer urlRecuperacionContrasenia = new StringBuffer( urlContext )
				.append(URL_RECUPERACION_CONTRASENIA)
				.append(token);
		
		return urlRecuperacionContrasenia.toString();
	}

}
