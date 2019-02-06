package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.List;

import mx.com.admoninmuebles.dto.RolDto;
import mx.com.admoninmuebles.persistence.model.Rol;

public interface RolService {
    Rol save(RolDto rolDto);
    
    RolDto findById(Long id);
    
    Collection<RolDto> findByNombres(List<String> nombres);

    Collection<RolDto> findAll();
    
    Collection<RolDto> getRolesSociosRepresentantes();
    
    Collection<RolDto> getRolesAdministradores();

}
