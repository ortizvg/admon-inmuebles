package mx.com.admoninmuebles.service;

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
import mx.com.admoninmuebles.dto.RolDto;
import mx.com.admoninmuebles.dto.SocioDto;
import mx.com.admoninmuebles.persistence.model.Rol;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.repository.RolRepository;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Rol save(final RolDto rolDto) {
        return rolRepository.save(modelMapper.map(rolDto, Rol.class));
    }

    @Override
    public Collection<RolDto> findAll() {
        return StreamSupport.stream(rolRepository.findAll().spliterator(), false).map(rol -> modelMapper.map(rol, RolDto.class)).collect(Collectors.toList());
    }
    
    @Override
    public Collection<RolDto> findByNombres(List<String> nombres) {
//    	Optional<Rol> rolOpt = rolRepository.findByNombre(name);
//    	if(rolOpt.isPresent()) {
//    		return modelMapper.map(rolOpt.get(), RolDto.class);
//    	}
//    	return null;
    	
    	return StreamSupport.stream(rolRepository.findByNombres(nombres).spliterator(), false).map(rol -> modelMapper.map(rol, RolDto.class)).collect(Collectors.toList());
    }
    
	@Override
	public Collection<RolDto> getRolesSociosRepresentantes() {
		 return StreamSupport.stream(rolRepository.findAll().spliterator(), false)
				 .filter(rol -> ( RolConst.ROLE_SOCIO_BI.equals(rol.getNombre())  )) 
				 .map(rol -> modelMapper.map(rol, RolDto.class))
				 .collect(Collectors.toList());
	}
	
	@Override
	public Collection<RolDto> getRolesAdministradores() {
		 return StreamSupport.stream(rolRepository.findAll().spliterator(), false)
				 .filter(rol -> RolConst.ROLE_CONTADOR.equals( rol.getNombre()) || ( RolConst.ROLE_ADMIN_BI.equals( rol.getNombre() ) || RolConst.ROLE_ADMIN_ZONA.equals( rol.getNombre() ) || RolConst.ROLE_ADMIN_CORP.equals( rol.getNombre() ) ) ) 
				 .map(rol -> modelMapper.map(rol, RolDto.class))
				 .collect(Collectors.toList());
	}

}
