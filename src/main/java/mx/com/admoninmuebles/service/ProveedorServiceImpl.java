package mx.com.admoninmuebles.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.AreaServicioDto;
import mx.com.admoninmuebles.dto.ProveedorDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.AreaServicio;
import mx.com.admoninmuebles.persistence.model.Comentario;
import mx.com.admoninmuebles.persistence.model.DatosAdicionales;
import mx.com.admoninmuebles.persistence.model.Rol;
import mx.com.admoninmuebles.persistence.model.Telefono;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.repository.AreaServicioRepository;
import mx.com.admoninmuebles.persistence.repository.ComentarioRepository;
import mx.com.admoninmuebles.persistence.repository.DatosAdicionalesRepository;
import mx.com.admoninmuebles.persistence.repository.DireccionRepository;
import mx.com.admoninmuebles.persistence.repository.RolRepository;
import mx.com.admoninmuebles.persistence.repository.TelefonoRepository;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;

@Service
public class ProveedorServiceImpl implements ProveedorService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RolRepository rolRepository;
	
	@Autowired
	private AreaServicioRepository areaServicioRepository;
	
	@Autowired
	private ComentarioRepository comentarioRepository;

	@Autowired
	private DireccionRepository direccionRepository;
	

	@Autowired
	private DatosAdicionalesRepository datosAdicionalesRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Collection<ProveedorDto> getProveedores() {
		Optional<Rol> rolOpt = rolRepository.findByNombre(RolConst.ROLE_PROVEEDOR);
		Collection<Usuario> proveedores = rolOpt.get().getUsuarios();
		return StreamSupport.stream(proveedores.spliterator(), false)
				 .map(proveedor -> modelMapper.map(proveedor, ProveedorDto.class))
				 .collect(Collectors.toList());
	}
	
	@Override
	public Collection<ProveedorDto>  buscarProveedorPorZona(String codigoZona) {
		Collection<Usuario> proveedores = usuarioRepository.findByDireccionAsentamientoZonaCodigo(codigoZona);
		 return StreamSupport.stream(proveedores.spliterator(), false)
				 .map(proveedor -> modelMapper.map(proveedor, ProveedorDto.class))
				 .collect(Collectors.toList());
	}

	@Override
	public ProveedorDto buscarProveedorPorId(Long idProveedor) {
		 Optional<Usuario> proveedor = usuarioRepository.findById(idProveedor);
	     return modelMapper.map(proveedor.get(), ProveedorDto.class);
	}

	@Override
	public ProveedorDto editar(ProveedorDto proveedorDto) {
		   Optional<Usuario> usuarioOptional = usuarioRepository.findById(proveedorDto.getId());
	        Usuario usuario = usuarioOptional.get();
	        usuario.setActivo(proveedorDto.isActivo());
	        
	        List<AreaServicio> areasServicio = StreamSupport.stream(proveedorDto.getAreasServicioSeleccionados().spliterator(), false)
					 .map(as -> areaServicioRepository.findById(as).get())
					 .collect(Collectors.toList());

	        usuario.setAreasServicio(areasServicio);
	        
	        if(proveedorDto.getApellidoMaterno() != null && !proveedorDto.getApellidoMaterno().isEmpty()) {
	        	usuario.setApellidoMaterno(proveedorDto.getApellidoMaterno() );
	        }
	        if(proveedorDto.getApellidoPaterno() != null && !proveedorDto.getApellidoPaterno().isEmpty()) {
	        	usuario.setApellidoPaterno(proveedorDto.getApellidoPaterno());
	        }
	        if(proveedorDto.getCorreo() != null && !proveedorDto.getCorreo().isEmpty()) {
	        	usuario.setCorreo(proveedorDto.getCorreo());
	        }
	        if(proveedorDto.getNombre() != null && !proveedorDto.getNombre().isEmpty()) {
	        	usuario.setNombre(proveedorDto.getNombre());
	        }
	        if(proveedorDto.getFacebook() != null && !proveedorDto.getFacebook().isEmpty()) {
	        	usuario.setFacebook(proveedorDto.getFacebook());
	        }
	        if(proveedorDto.getFotoUrl() != null && !proveedorDto.getFotoUrl().isEmpty()) {
	        	usuario.setFotoUrl(proveedorDto.getFotoUrl());
	        }
	        if(proveedorDto.getGoogleMapsDir() != null && !proveedorDto.getGoogleMapsDir().isEmpty()) {
	        	usuario.setGoogleMapsDir(proveedorDto.getGoogleMapsDir());
	        }
	        if( proveedorDto.getTwiter() != null && !proveedorDto.getTwiter().isEmpty()) {
	        	usuario.setTwiter(proveedorDto.getTwiter());
	        }
	        if(proveedorDto.getYoutube() != null && !proveedorDto.getYoutube().isEmpty()) {
	        	usuario.setYoutube(proveedorDto.getYoutube());
	        }
	        if(proveedorDto.getTelefonoAlternativo() != null && !proveedorDto.getTelefonoAlternativo().isEmpty()) {
	        	usuario.setTelefonoAlternativo(proveedorDto.getTelefonoAlternativo());
	        }
	        if(proveedorDto.getTelefonoFijo() != null && !proveedorDto.getTelefonoFijo().isEmpty()) {
	        	usuario.setTelefonoFijo(proveedorDto.getTelefonoFijo());
	        }
	        if(proveedorDto.getTelefonoMovil() != null && !proveedorDto.getTelefonoMovil().isEmpty()) {
	        	usuario.setTelefonoMovil(proveedorDto.getTelefonoMovil());
	        }
	        if(proveedorDto.getTelefonoOficina() != null && !proveedorDto.getTelefonoOficina().isEmpty()) {
	        	usuario.setTelefonoOficina(proveedorDto.getTelefonoOficina());
	        }
	        if(proveedorDto.getDatosDomicilio() != null && !proveedorDto.getDatosDomicilio().isEmpty()) {
	        	usuario.setDatosDomicilio(proveedorDto.getDatosDomicilio());
	        }
	        
			if(proveedorDto.getComentario() != null && !proveedorDto.getComentario().trim().isEmpty()) {
				comentarioRepository.save(new Comentario(proveedorDto.getComentario(), usuario));
			}
			
			DatosAdicionales datosAdicionales = modelMapper.map(proveedorDto, Usuario.class).getDatosAdicionales();
			usuario.setDatosAdicionales(datosAdicionalesRepository.save(datosAdicionales));
			
			usuario.setDireccion(direccionRepository.save(modelMapper.map(proveedorDto, Usuario.class).getDireccion()));
	        
	        Usuario usuarioActualizado = usuarioRepository.save(usuario);
	        return modelMapper.map(usuarioActualizado, ProveedorDto.class);
	}
	
	
	@Override
	public ProveedorDto guardar(ProveedorDto proveedorDto) {
    	Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(proveedorDto.getUsername());
        if (usuarioOptional.isPresent()) {
            throw new BusinessException("usuario.error.yaexiste");
        }
		Usuario proveedor = modelMapper.map(proveedorDto, Usuario.class);
		Rol rol = rolRepository.findByNombre(RolConst.ROLE_PROVEEDOR).get();
		List<Rol> rolProveedor = new ArrayList<>();
		rolProveedor.add(rol);
		
		List<AreaServicio> areasServicio = StreamSupport.stream(proveedorDto.getAreasServicioSeleccionados().spliterator(), false)
				 .map(as -> areaServicioRepository.findById(as).get())
				 .collect(Collectors.toList());

		proveedor.setAreasServicio(areasServicio);
		proveedor.setRoles(rolProveedor);
		proveedor.setDireccion(direccionRepository.save(proveedor.getDireccion()));
		proveedor.setDatosAdicionales(datosAdicionalesRepository.save(proveedor.getDatosAdicionales()));
		
		Usuario proveedorCreado = usuarioRepository.save(proveedor);
		
		if(proveedorDto.getComentario() != null && !proveedorDto.getComentario().trim().isEmpty()) {
			comentarioRepository.save(new Comentario(proveedorDto.getComentario(), proveedorCreado));
		}
		
		return modelMapper.map(proveedorCreado, ProveedorDto.class);
	}


	@Override
	public void eliminar(Long idProveedor) {
		usuarioRepository.deleteById(idProveedor);

	}
	
    @Override
    public boolean exist(final Long id) {
        return usuarioRepository.existsById(id);
    }

}
