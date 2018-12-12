package mx.com.admoninmuebles.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.SocioDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.persistence.model.Rol;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.repository.RolRepository;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;

@Service
public class SocioServiceImpl implements SocioService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RolRepository rolRepository;
	

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Collection<SocioDto> getSocios() {
		Optional<Rol> rolSocioBiOpt = rolRepository.findByNombre(RolConst.ROLE_SOCIO_BI);
		Collection<Usuario> sociosBi = rolSocioBiOpt.get().getUsuarios();
		
		Optional<Rol> rolRepBiOpt = rolRepository.findByNombre(RolConst.ROLE_REP_BI);
		Collection<Usuario> representantesBi = rolRepBiOpt.get().getUsuarios();
		
		Stream<Usuario> socios = Stream.of(sociosBi, representantesBi).flatMap(Collection::stream);
		
		return socios
				 .map(socio -> modelMapper.map(socio, SocioDto.class))
				 .collect(Collectors.toList());
	}
	

	@Override
	public SocioDto buscarSocioPorId(Long idSocio) {
		 Optional<Usuario> socio = usuarioRepository.findById(idSocio);
	     return modelMapper.map(socio.get(), SocioDto.class);
	}

	@Override
	public SocioDto guardar(SocioDto socioDto) {
		Usuario socio = modelMapper.map(socioDto, Usuario.class);
		
		 Collection<Rol> roles = new ArrayList<>();
	        roles.add(rolRepository.findById(socioDto.getRolSeleccionado()).get());
	        socio.setRoles(roles);
		Usuario socioCreado = usuarioRepository.save(socio);
		
		return modelMapper.map(socioCreado, SocioDto.class);
	}

	@Override
	public void eliminar(Long idSocio) {
		usuarioRepository.deleteById(idSocio);
		
	}

	@Override
	public boolean exist(Long id) {
		return usuarioRepository.existsById(id);
	}
	
	@Override
	public Collection<UsuarioDto> findSociosByZonaCodigo(String zonaCodigo) {
		return StreamSupport.stream(usuarioRepository.findSociosByZonaCodigo(zonaCodigo).spliterator(), false).map(usuario -> modelMapper.map(usuario, UsuarioDto.class)).collect(Collectors.toList());
	}


	@Override
	public Collection<UsuarioDto> findSociosByAdminBiId(Long id) {
		return StreamSupport.stream(usuarioRepository.findSociosByAdminBiId(id).spliterator(), false).map(usuario -> modelMapper.map(usuario, UsuarioDto.class)).collect(Collectors.toList());
	}
	


}
