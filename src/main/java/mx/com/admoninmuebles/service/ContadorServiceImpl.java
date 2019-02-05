package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;

@Service
public class ContadorServiceImpl implements ContadorService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired UsuarioService usuarioService;
	
    @Autowired
    private ModelMapper modelMapper;
	
	public Collection<UsuarioDto> buscarTodos() {
		 return StreamSupport.stream(usuarioRepository.findByRolesNombre(RolConst.ROLE_CONTADOR) .spliterator(), false).map(usuario -> modelMapper.map(usuario, UsuarioDto.class)).collect(Collectors.toList());
	}

}
