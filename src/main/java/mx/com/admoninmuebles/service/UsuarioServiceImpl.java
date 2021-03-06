package mx.com.admoninmuebles.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.CambioContraseniaDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Inmueble;
import mx.com.admoninmuebles.persistence.model.Rol;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.model.Zona;
import mx.com.admoninmuebles.persistence.repository.InmuebleRepository;
import mx.com.admoninmuebles.persistence.repository.RolRepository;
import mx.com.admoninmuebles.persistence.repository.TipoSocioRepository;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;
import mx.com.admoninmuebles.persistence.repository.ZonaRepository;
import mx.com.admoninmuebles.security.SecurityUtils;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Autowired
    private UsuarioRepository userRepository;
    
    @Autowired
    private InmuebleRepository inmuebleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ZonaRepository zonaRepository;
    
    @Autowired
    private TipoSocioRepository tipoSocioRepository;
    
    @Autowired
    private CorreoUsuarioService correoUsuarioService;
    
    @Autowired
    private ZonaService zonaService;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private InmuebleService inmuebleService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UsuarioDto crearCuenta(final UsuarioDto userDto) {
        Optional<Usuario> usuarioOptional = userRepository.findByUsername(userDto.getUsername());
        if (usuarioOptional.isPresent()) {
            throw new BusinessException("usuario.error.yaexiste");
        }
        
        String contrasenia = RandomStringUtils.randomAlphanumeric(8);
        userDto.setContrasenia(contrasenia);
        Usuario usuario = modelMapper.map(userDto, Usuario.class);

        Collection<Rol> roles = new ArrayList<>();
        roles.add(rolRepository.findById(userDto.getRolSeleccionado()).get());
        usuario.setRoles(roles);
        Usuario usuarioCreado = userRepository.save(usuario);
        
        if( RolConst.ROLE_SOCIO_BI.equals(usuarioCreado.getRoles().stream().findFirst().get().getNombre() ) ) {
        	usuarioCreado = crearReferenciaCuentaPagoSocios(usuarioCreado, userDto.getInmuebleId());
        }
        
        if( RolConst.ROLE_ADMIN_BI.equals(usuarioCreado.getRoles().stream().findFirst().get().getNombre() ) ) {
        	Optional<Zona> zonaOpt = zonaRepository.findById(userDto.getZonaSeleccionado());
        	if( zonaOpt.isPresent() ) {
        		Zona zona = zonaOpt.get();
        		zona.addAdminBi(usuarioCreado);
        		zonaRepository.save(zona);
        	}
        }
        
        return modelMapper.map(usuarioCreado, UsuarioDto.class);
    }
    
    private Usuario crearReferenciaCuentaPagoSocios(Usuario socio, Long idInmueble) {
    	Inmueble inmueble = inmuebleRepository.findById(idInmueble).get();
    	socio.setReferenciaPagoSocio(socio.getId() + "-" + inmueble.getDatosAdicionales().getNumeroCuenta());
    	socio.setCuentaPagoSocio( inmueble.getDatosAdicionales().getNumeroCuenta() );
    	return userRepository.save(socio);
    }

    @Override
    public UsuarioDto editarCuenta(final UsuarioDto userDto) {
        Optional<Usuario> usuarioOptional = userRepository.findById(userDto.getId());
        Usuario usuario = usuarioOptional.get();

        Collection<Rol> roles = new ArrayList<>();
        roles.add(rolRepository.findById(userDto.getRolSeleccionado()).get());
        usuario.setRoles(roles);
        usuario.setActivo(userDto.isActivo());

        if (userDto.getApellidoMaterno() != null && !userDto.getApellidoMaterno().isEmpty()) {
            usuario.setApellidoMaterno(userDto.getApellidoMaterno());
        }
        if (userDto.getApellidoPaterno() != null && !userDto.getApellidoPaterno().isEmpty()) {
            usuario.setApellidoPaterno(userDto.getApellidoPaterno());
        }
        if (userDto.getCorreo() != null && !userDto.getCorreo().isEmpty()) {
            usuario.setCorreo(userDto.getCorreo());
        }
        if (userDto.getCorreoAlternativo1() != null && !userDto.getCorreoAlternativo1().isEmpty()) {
            usuario.setCorreoAlternativo1(userDto.getCorreoAlternativo1());
        }
        
        if (userDto.getCorreoAlternativo2() != null && !userDto.getCorreoAlternativo2().isEmpty()) {
            usuario.setCorreoAlternativo2(userDto.getCorreoAlternativo2());
        }
        if (userDto.getTipoSocioId() != null) {
            usuario.setTipoSocio(tipoSocioRepository.findById(userDto.getTipoSocioId()).get());
        }
        if (userDto.getNombre() != null && !userDto.getNombre().isEmpty()) {
            usuario.setNombre(userDto.getNombre());
        }
        if (userDto.getFacebook() != null && !userDto.getFacebook().isEmpty()) {
            usuario.setFacebook(userDto.getFacebook());
        }
        if (userDto.getFotoUrl() != null && !userDto.getFotoUrl().isEmpty()) {
            usuario.setFotoUrl(userDto.getFotoUrl());
        }
        if (userDto.getGoogleMapsDir() != null && !userDto.getGoogleMapsDir().isEmpty()) {
            usuario.setGoogleMapsDir(userDto.getGoogleMapsDir());
        }
        if (userDto.getTwiter() != null && !userDto.getTwiter().isEmpty()) {
            usuario.setTwiter(userDto.getTwiter());
        }
        if (userDto.getYoutube() != null && !userDto.getYoutube().isEmpty()) {
            usuario.setYoutube(userDto.getYoutube());
        }
        if (userDto.getTelefonoAlternativo() != null && !userDto.getTelefonoAlternativo().isEmpty()) {
            usuario.setTelefonoAlternativo(userDto.getTelefonoAlternativo());
        }
        if (userDto.getTelefonoFijo() != null && !userDto.getTelefonoFijo().isEmpty()) {
            usuario.setTelefonoFijo(userDto.getTelefonoFijo());
        }
        if (userDto.getTelefonoMovil() != null && !userDto.getTelefonoMovil().isEmpty()) {
            usuario.setTelefonoMovil(userDto.getTelefonoMovil());
        }
        if (userDto.getTelefonoOficina() != null && !userDto.getTelefonoOficina().isEmpty()) {
            usuario.setTelefonoOficina(userDto.getTelefonoOficina());
        }
        if (userDto.getDatosDomicilio() != null && !userDto.getDatosDomicilio().isEmpty()) {
            usuario.setDatosDomicilio(userDto.getDatosDomicilio());
        }
        
        if (roles.stream().anyMatch(rol -> RolConst.ROLE_SOCIO_BI.equals(rol.getNombre()))) {

            if (userDto.getInmuebleId() != null) {
                // usuario.setInmueble(inmuebleRepository.findById(userDto.getInmuebleId()).get());
            }

        }

        Usuario usuarioActualizado = userRepository.save(usuario);
        
        if( RolConst.ROLE_ADMIN_BI.equals(usuarioActualizado.getRoles().stream().findFirst().get().getNombre() ) ) {
        	 Optional<Zona> zonaOpt = zonaRepository.findByAdministradorBiId( userDto.getId() );
        	if( userDto.getZonaSeleccionado() != null &&  StringUtils.isNotBlank( userDto.getZonaSeleccionado() ) && zonaOpt.isPresent() ) {
        		zonaRepository.updateZonaAdministradoresBi( userDto.getZonaSeleccionado() , userDto.getId() );
        	}
        	
        	if(userDto.getZonaSeleccionado() != null &&  StringUtils.isNotBlank( userDto.getZonaSeleccionado() ) && !zonaOpt.isPresent() ) {
        		Zona zona = zonaRepository.findById( userDto.getZonaSeleccionado() ).get();
        		zona.addAdminBi(usuarioActualizado);
        		zonaRepository.save(zona);
        	}
        }
        
        return modelMapper.map(usuarioActualizado, UsuarioDto.class);
    }

    @Override
    public Collection<UsuarioDto> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).map(usuario -> modelMapper.map(usuario, UsuarioDto.class)).collect(Collectors.toList());
    }

    @Override
    public UsuarioDto editarPerfil(final UsuarioDto userDto) {
        String usuarioAutenticado = SecurityUtils.getCurrentUserLogin().get();
        Optional<Usuario> usuarioOptional = userRepository.findByUsername(usuarioAutenticado);

        Usuario usuario = usuarioOptional.get();

        if (userDto.getApellidoMaterno() != null && !userDto.getApellidoMaterno().isEmpty()) {
            usuario.setApellidoMaterno(userDto.getApellidoMaterno());
        }
        if (userDto.getApellidoPaterno() != null && !userDto.getApellidoPaterno().isEmpty()) {
            usuario.setApellidoPaterno(userDto.getApellidoPaterno());
        }
        if (userDto.getCorreo() != null && !userDto.getCorreo().isEmpty()) {
            usuario.setCorreo(userDto.getCorreo());
        }
        if (userDto.getNombre() != null && !userDto.getNombre().isEmpty()) {
            usuario.setNombre(userDto.getNombre());
        }
        if (userDto.getFacebook() != null && !userDto.getFacebook().isEmpty()) {
            usuario.setFacebook(userDto.getFacebook());
        }
        if (userDto.getFotoUrl() != null && !userDto.getFotoUrl().isEmpty()) {
            usuario.setFotoUrl(userDto.getFotoUrl());
        }
        if (userDto.getGoogleMapsDir() != null && !userDto.getGoogleMapsDir().isEmpty()) {
            usuario.setGoogleMapsDir(userDto.getGoogleMapsDir());
        }
        if (userDto.getTwiter() != null && !userDto.getTwiter().isEmpty()) {
            usuario.setTwiter(userDto.getTwiter());
        }
        if (userDto.getYoutube() != null && !userDto.getYoutube().isEmpty()) {
            usuario.setYoutube(userDto.getYoutube());
        }
        if (userDto.getTelefonoAlternativo() != null && !userDto.getTelefonoAlternativo().isEmpty()) {
            usuario.setTelefonoAlternativo(userDto.getTelefonoAlternativo());
        }
        if (userDto.getTelefonoFijo() != null && !userDto.getTelefonoFijo().isEmpty()) {
            usuario.setTelefonoFijo(userDto.getTelefonoFijo());
        }
        if (userDto.getTelefonoMovil() != null && !userDto.getTelefonoMovil().isEmpty()) {
            usuario.setTelefonoMovil(userDto.getTelefonoMovil());
        }
        if (userDto.getTelefonoOficina() != null && !userDto.getTelefonoOficina().isEmpty()) {
            usuario.setTelefonoOficina(userDto.getTelefonoOficina());
        }

        Usuario usuarioActualizado = userRepository.save(usuario);
        return modelMapper.map(usuarioActualizado, UsuarioDto.class);
    }

    @Override
    public UsuarioDto findById(final Long idUsuario) {
        Optional<Usuario> usuarioOptional = userRepository.findById(idUsuario);

        if (!usuarioOptional.isPresent()) {
        	throw new BusinessException("usuario.error.noencontrado");
        }

        return modelMapper.map(usuarioOptional.get(), UsuarioDto.class);
    }

    @Override
    public UsuarioDto findUserLogin() {
        String usuarioAutenticado = SecurityUtils.getCurrentUserLogin().get();
        Optional<Usuario> usuarioOptional = userRepository.findByUsername(usuarioAutenticado);

        return modelMapper.map(usuarioOptional.get(), UsuarioDto.class);
    }

    @Transactional
    @Override
    public void deleteById(final Long idUsuario) {
		Optional<Usuario> usuarioOptional = userRepository.findById(idUsuario);
		Rol rolUsuarioEliminar = rolRepository.findByUserId( idUsuario ).get() ;
		
		if (!usuarioOptional.isPresent()) {
			throw new BusinessException("usuario.error.noencontrado");
		}
		
		if( RolConst.ROLE_CONTADOR.equals( rolUsuarioEliminar.getNombre()  ) ) {
			Collection<InmuebleDto> inmuebles = inmuebleService.findByContadorId( idUsuario );
			if( !inmuebles.isEmpty() ) {
				throw new DataIntegrityViolationException("exception.data.integrity.violation");
			}
		}
		
		if( RolConst.ROLE_ADMIN_ZONA.equals( rolUsuarioEliminar.getNombre() ) ) {
			zonaService.deleteByAdminZonaId( idUsuario );
		}
		
		if( RolConst.ROLE_ADMIN_BI.equals( rolUsuarioEliminar.getNombre() ) ) {
			inmuebleService.deleteByAdminBiId( idUsuario );
			zonaRepository.deleteAdminBiZonaByAdminBiId( idUsuario );
		}
		
		userRepository.deleteById(idUsuario);

    }

    @Override
    public UsuarioDto cambiarContrasenia(final CambioContraseniaDto cambioContraseniaDto) {
        String usuarioAutenticado = SecurityUtils.getCurrentUserLogin().get();
        Optional<Usuario> usuarioOptional = userRepository.findByUsername(usuarioAutenticado);

        if (!usuarioOptional.isPresent()) {
            throw new BusinessException("usuario.error.noencontrado");
        }
        Usuario usuarioLogin = usuarioOptional.get();

        if (!passwordEncoder.matches(cambioContraseniaDto.getContraseniaAnterior(), usuarioLogin.getContrasenia())) {
            throw new BusinessException("usuario.actualiza.contrasenia.anterior.novalida");
        }

        if (!cambioContraseniaDto.getContraseniaNueva().equals(cambioContraseniaDto.getContraseniaConfirmacion())) {
            throw new BusinessException("usuario.actualiza.contrasenias.noiguales");
        }

        usuarioLogin.setContrasenia(passwordEncoder.encode(cambioContraseniaDto.getContraseniaNueva()));

        userRepository.save(usuarioLogin);

        return modelMapper.map(usuarioLogin, UsuarioDto.class);

    }

    @Override
    public UsuarioDto findByUsernameOrCorreo(final String usernameCorreo) {

        Optional<Usuario> usuarioOptional;

        if (new EmailValidator().isValid(usernameCorreo, null)) {
            usuarioOptional = userRepository.findByCorreo(usernameCorreo);
        } else {
            usuarioOptional = userRepository.findByUsername(usernameCorreo);
        }

        if (!usuarioOptional.isPresent()) {
            throw new UsernameNotFoundException("usuario.error.noencontrado");
        }

        return modelMapper.map(usuarioOptional.get(), UsuarioDto.class);
    }

    @Override
    public Collection<UsuarioDto> findByRolesNombre(final String nombre) {
        return StreamSupport.stream(userRepository.findByRolesNombre(nombre).spliterator(), false).map(usuario -> modelMapper.map(usuario, UsuarioDto.class)).collect(Collectors.toList());

    }

    @Override
    public Collection<UsuarioDto> findByRolesNombreAndAreasServicioId(final String nombre, final Long id) {
        return StreamSupport.stream(userRepository.findByRolesNombreAndAreasServicioId(nombre, id).spliterator(), false).map(usuario -> modelMapper.map(usuario, UsuarioDto.class)).collect(Collectors.toList());
    }

    @Override
    public Collection<UsuarioDto> findAllAdministradores() {
        List<Rol> roles = StreamSupport.stream(rolRepository.findAll().spliterator(), false)
                .filter(rol ->  (RolConst.ROLE_ADMIN_BI.equals(rol.getNombre()) || RolConst.ROLE_ADMIN_ZONA.equals(rol.getNombre()) ))
                .collect(Collectors.toList());

        return StreamSupport.stream(userRepository.findByRolesIn(roles).spliterator(), false).map(usuario -> modelMapper.map(usuario, UsuarioDto.class)).collect(Collectors.toList());
    }

    @Override
    public Collection<UsuarioDto> findAdministradoresBiByZonaCodigo(final String zonaCodigo) {
        Optional<Zona> zona = zonaRepository.findById(zonaCodigo);
        Collection<UsuarioDto> administradoresBi = Collections.emptyList();
        if (zona.isPresent()) {
            administradoresBi = StreamSupport.stream(zona.get().getAdministradoresBi().spliterator(), false).map(usuario -> modelMapper.map(usuario, UsuarioDto.class)).collect(Collectors.toList());
        }
        return administradoresBi;
    }
    
	@Async
	@Override
	public void enviarCorreoActivacion(UsuarioDto usuario, final String urlContext) {
			correoUsuarioService.enviarActivacion(usuario, urlContext);
	}
	
	@Async
	@Override
	public void enviarCorreoRecuperacionContrasenia(UsuarioDto usuario, final String urlContext) {
			correoUsuarioService.enviarRecuperacionContrasenia(usuario, urlContext);
	}

	@Override
	public Collection<UsuarioDto> findAdminsBiByAdminZona(Long id) {
		 return StreamSupport.stream(userRepository.findAdministradoresBiByAdminZona( id ).spliterator(), false).map(usuario -> modelMapper.map(usuario, UsuarioDto.class)).collect(Collectors.toList());
	}

}
