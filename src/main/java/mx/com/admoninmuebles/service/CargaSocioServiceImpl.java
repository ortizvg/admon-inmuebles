package mx.com.admoninmuebles.service;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.SimbolosConst;
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

	/**
	 * Lee archivo para carga de usuarios en 
	 * base de datos.
	 */
	@Override
	public CargaSocioDto validaCSVSocios(BufferedReader br) {
		String line = null;
		CargaSocioDto socio = new CargaSocioDto();
		int contadorLinea = 0;
		try {
			while((line = br.readLine()) != null) {
				contadorLinea++;
				String[] arrayStr = line.split(SimbolosConst.COMA);
				agregarSocio(arrayStr, contadorLinea, socio);
			}
		} catch (Exception e) {
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
	private CargaSocioDto agregarSocio(String[] arrayStr, int contador, CargaSocioDto cargaSocio) {
		UsuarioDto socio = new UsuarioDto();
		List<UsuarioDto> listaSocios = null;
		List<ErrorDto> listaErrores = null;
		String mensajeError = "Error en la línea ";
		try {
			socio.setUsername(arrayStr[SociosConst.USER_NAME]);
			socio = validaUserName(socio);
			socio.setCorreo(arrayStr[SociosConst.CORREO]);
			socio.setNombre(arrayStr[SociosConst.NOMBRE]);
			socio.setApellidoPaterno(arrayStr[SociosConst.APELLIDO_PATERNO]);
			socio.setApellidoMaterno(arrayStr[SociosConst.APELLIDO_MATERNO]);
			socio.setCoutaMensualPagoSocio(new BigDecimal(arrayStr[SociosConst.CUOTA_MENSUAL_PAGO_SOCIO]));
			socio.setCorreoAlternativo1(arrayStr[SociosConst.CORREO_ALTERNATIVO_1]);
			socio.setCorreoAlternativo2(arrayStr[SociosConst.CORREO_ALTERNATIVO_2]);
			socio.setRolSeleccionado(Long.valueOf(arrayStr[SociosConst.ROL_ID]));
			socio.setTipoSocioId( Long.valueOf(arrayStr[SociosConst.TIPO_SOCIO]) );
			socio.setDatosDomicilio(arrayStr[SociosConst.DATOS_DOMICILIO]);
			socio.setInmuebleId(Long.valueOf(arrayStr[SociosConst.INMUEBLE_ID]));
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
			logger.error("Dato sin podeer leer");
			if(cargaSocio.getListaErrores() == null || cargaSocio.getListaErrores().isEmpty()) {
				listaErrores = new ArrayList<ErrorDto>();
				ErrorDto errorDto = new ErrorDto();
				errorDto.setId(contador);
				errorDto.setMessage(mensajeError.concat(String.valueOf(contador)).concat(" "+e.getMessage()));
				listaErrores.add(errorDto);
				cargaSocio.setListaErrores(listaErrores);
			}else {
				listaErrores = cargaSocio.getListaErrores();
				ErrorDto errorDto = new ErrorDto();
				errorDto.setId(contador);
				errorDto.setMessage(mensajeError.concat(String.valueOf(contador)));
				listaErrores.add(errorDto);
				cargaSocio.setListaErrores(listaErrores);
			}
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

}
