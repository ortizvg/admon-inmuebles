package mx.com.admoninmuebles.service;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
			guardaSocioMasivo(socio.getListaSocios());
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
		String mensajeError = "Error en la línea: ";
		try {
			socio.setUsername(arrayStr[SociosConst.USER_NAME]);
			socio = validaUserName(socio);
			socio.setNombre(arrayStr[SociosConst.NOMBRE]);
			socio.setApellidoPaterno(arrayStr[SociosConst.APELLIDO_PATERNO]);
			socio.setApellidoMaterno(arrayStr[SociosConst.APELLIDO_MATERNO]);
			socio.setCorreo(arrayStr[SociosConst.CORREO]);
			socio.setGoogleMapsDir(arrayStr[SociosConst.MAPA_DIRECCION]);
			socio.setDatosDomicilio(arrayStr[SociosConst.DATOS_DOMICILIO]);
			socio.setTelefonoOficina(arrayStr[SociosConst.TELEFONO_OFICINA]);
			socio.setTelefonoMovil(arrayStr[SociosConst.TELEFONO_MOVIL]);
			socio.setTelefonoAlternativo(arrayStr[SociosConst.TELEFONO_ALTERNATIVO]);
			socio.setInmuebleId(Long.valueOf(arrayStr[SociosConst.INMUEBLE_ID]));
			socio.setRolSeleccionado(Long.valueOf(arrayStr[SociosConst.ROL_ID]));
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
	 * si no existe le asigna una contrasenia
	 * @param socio
	 * @return
	 */
	private UsuarioDto validaUserName(UsuarioDto socio) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(socio.getUsername());
        if (usuarioOptional.isPresent()) {
            throw new BusinessException("El usuario ya existe");
        }
        String contrasenia = RandomStringUtils.randomAlphanumeric(8);
        socio.setContrasenia(contrasenia);
        return socio;
		
	}
	
	/**
	 * Guarda varios socios con sus
	 * respectivos roles
	 * @param listaSocios
	 */
	private void guardaSocioMasivo(List<UsuarioDto> listaSocios) {
		for (UsuarioDto usuarioDto : listaSocios) {
	        Usuario usuario = modelMapper.map(usuarioDto, Usuario.class);

	        Collection<Rol> roles = new ArrayList<>();
	        roles.add(rolRepository.findById(usuarioDto.getRolSeleccionado()).get());
	        usuario.setRoles(roles);
	        Usuario usuarioCreado = usuarioRepository.save(usuario);
	        UsuarioDto usuarioDtoCreado = modelMapper.map(usuarioCreado, UsuarioDto.class);
	        guardaInmueble(usuarioDtoCreado, usuarioDto.getInmuebleId());
		}
	}

	/**
	 * Guarda el inmueble asociado al 
	 * usuario creado
	 * @param usuarioDtoCreado
	 * @param inmuebleId
	 */
	private void guardaInmueble(UsuarioDto usuarioDtoCreado, Long inmuebleId) {
		Inmueble inmueble = inmuebleRepository.findById(inmuebleId).get();
		Usuario socio = usuarioRepository.findById(usuarioDtoCreado.getId()).get();
		inmueble.addSocio(socio);
		inmuebleRepository.save(inmueble);
		
	}

}
