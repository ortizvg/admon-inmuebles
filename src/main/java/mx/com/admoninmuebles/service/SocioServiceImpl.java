package mx.com.admoninmuebles.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.controller.SocioController;
import mx.com.admoninmuebles.dto.SocioDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.persistence.model.Inmueble;
import mx.com.admoninmuebles.persistence.model.Rol;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.repository.InmuebleRepository;
import mx.com.admoninmuebles.persistence.repository.RolRepository;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;

@Service
public class SocioServiceImpl implements SocioService{
	
	Logger logger = LoggerFactory.getLogger(SocioServiceImpl.class);
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RolRepository rolRepository;
	
	@Autowired
	private InmuebleRepository inmuebleRepository;
	

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Collection<SocioDto> getSocios() {
		Optional<Rol> rolSocioBiOpt = rolRepository.findByNombre(RolConst.ROLE_SOCIO_BI);
		Collection<Usuario> sociosBi = rolSocioBiOpt.get().getUsuarios();
		
		return sociosBi.stream()
				 .map(socio -> {
					    SocioDto usuarioDto = modelMapper.map(socio, SocioDto.class);
						Inmueble inmueble = inmuebleRepository.findBySocioId( usuarioDto.getId() );
						usuarioDto.setInmuebleId( inmueble.getId() );
						usuarioDto.setInmuebleNombre( inmueble.getNombre() );
						return usuarioDto;
						})
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

	@Transactional
	@Override
	public void eliminar(Long idSocio) {
		if( isSocioInInmuebleBySocioId( idSocio ) ) {
			logger.info("Esta asociado: ");
			usuarioRepository.deleteInmuelesSociosBySocioId( idSocio );
		}
		usuarioRepository.deleteById(idSocio);
		
	}

	@Override
	public boolean exist(Long id) {
		return usuarioRepository.existsById(id);
	}
	
	@Override
	public Collection<UsuarioDto> findSociosByZonaCodigo(String zonaCodigo) {
		return StreamSupport.stream(usuarioRepository.findSociosByZonaCodigo(zonaCodigo).spliterator(), false)
				.map(usuario -> {
					UsuarioDto usuarioDto = modelMapper.map(usuario, UsuarioDto.class);
					Inmueble inmueble = inmuebleRepository.findBySocioId( usuarioDto.getId() );
					usuarioDto.setInmuebleId( inmueble.getId() );
					usuarioDto.setInmuebleNombre( inmueble.getNombre() );
					return usuarioDto;
					})
				.collect(Collectors.toList());
	}


	@Override
	public Collection<UsuarioDto> findSociosByAdminBiId(Long id) {
		return StreamSupport.stream(usuarioRepository.findSociosByAdminBiId(id).spliterator(), false)
				.map(usuario -> {
					UsuarioDto usuarioDto = modelMapper.map(usuario, UsuarioDto.class);
					Inmueble inmueble = inmuebleRepository.findBySocioId( usuarioDto.getId() );
					usuarioDto.setInmuebleId( inmueble.getId() );
					usuarioDto.setInmuebleNombre( inmueble.getNombre() );
					return usuarioDto;
					})
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<UsuarioDto> findSociosByAdminZonaId(Long id) {
		return StreamSupport.stream(usuarioRepository.findSociosByAdminZonaId(id).spliterator(), false)
				.map(usuario -> {
					UsuarioDto usuarioDto = modelMapper.map(usuario, UsuarioDto.class);
					Inmueble inmueble = inmuebleRepository.findBySocioId( usuarioDto.getId() );
					usuarioDto.setInmuebleId( inmueble.getId() );
					usuarioDto.setInmuebleNombre( inmueble.getNombre() );
					return usuarioDto;
					})
				.collect(Collectors.toList());
	}


	@Override
	public boolean isSocioInInmuebleBySocioId(Long id) {
		logger.info("BusquedaSocioInmueble ID: " + id);
		Long numeroRegistros = usuarioRepository.countInmuelesSociosBySocioId(id);
		logger.info("numeroRegistros " + numeroRegistros);
		if( numeroRegistros == 0 ) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	


}
