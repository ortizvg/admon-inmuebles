package mx.com.admoninmuebles.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.ContadorDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Rol;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.repository.RolRepository;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;

@Service
public class ContadorServiceImpl implements ContadorService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired UsuarioService usuarioService;
	
	@Autowired
    private UsuarioRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ModelMapper modelMapper;
	
	public Collection<UsuarioDto> buscarTodos() {
		 return StreamSupport.stream(usuarioRepository.findByRolesNombre(RolConst.ROLE_CONTADOR) .spliterator(), false).map(usuario -> modelMapper.map(usuario, UsuarioDto.class)).collect(Collectors.toList());
	}

	@Override
	public ContadorDto buscarContadorPorId(Long idContador) {
		 Optional<Usuario> contadorOpt = usuarioRepository.findById(idContador);
		 if (contadorOpt.isPresent()) {
	            throw new BusinessException("usuario.error.no.existe");
	        }
	     return modelMapper.map(contadorOpt.get(), ContadorDto.class);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public ContadorDto guardar(ContadorDto contadorDto) {
		Usuario contador = modelMapper.map(contadorDto, Usuario.class);
		
		 Collection<Rol> roles = new ArrayList<>();
	        roles.add(rolRepository.findByNombre(RolConst.ROLE_CONTADOR).get());
	        contador.setRoles(roles);
		Usuario contadorCreado = usuarioRepository.save(contador);
		
		return modelMapper.map(contadorCreado, ContadorDto.class);
	}

	@Override
	public void eliminar(Long idContador) {
		if( usuarioRepository.existsById(idContador) ) {
			usuarioRepository.deleteById(idContador);
		}
	}

	@Override
	public boolean exist(Long id) {
		return usuarioRepository.existsById(id);
	}

}
