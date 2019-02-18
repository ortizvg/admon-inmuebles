package mx.com.admoninmuebles.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import mx.com.admoninmuebles.constant.PlantillaCorreoConst;
import mx.com.admoninmuebles.dto.CorreoDto;
import mx.com.admoninmuebles.dto.UsuarioDto;

@Service
public class CorreoUsuarioServiceImpl implements CorreoUsuarioService {
	
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
    private Environment env;

	@Override
	public void enviarActivacion( final UsuarioDto usuarioDto, final String urlContext ) {
		String urlActiacion = getUrlActivacion( usuarioDto, urlContext );
		
		Context datosPlantilla = new Context();
		datosPlantilla.setVariable( PARAMETRO_CORREO_URL_ACTIVACION, urlActiacion );
		datosPlantilla.setVariable( "nombre", usuarioDto.getNombre() );
		
		CorreoDto correoDto = new CorreoDto();
		correoDto.setDe( env.getProperty( PROPIEDAD_CORREO_USUARIOS_DE ) );
//		correoDto.setAsunto( env.getProperty( PROPIEDAD_CUENTA_ACTIVACION_ASUNTO ) );
		correoDto.setAsunto( "Activación de cuenta" );
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
		
		CorreoDto correoDto = new CorreoDto();
		correoDto.setDe( env.getProperty( PROPIEDAD_CORREO_USUARIOS_DE ) );
//		correoDto.setAsunto( env.getProperty( PROPIEDAD_CUENTA_RECUPERACION_CONTRASENIA_ASUNTO ) );
		correoDto.setAsunto( "Recuperacion de contraseña" );
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
