package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.TicketDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.persistence.model.Inmueble;

public interface InmuebleService {
    Inmueble save(InmuebleDto inmuebleDto);

    Collection<InmuebleDto> findAll();
    
    Collection<InmuebleDto> findByAdminBiId(Long id);
    
    Collection<InmuebleDto> findByDireccionAsentamientoId(Long id);
    
    Collection<UsuarioDto> findSociosByInmuebleId(Long id);
    
    Collection<TicketDto> findTicketsByInmuebleId(Long id);

    InmuebleDto findById(Long id);

    void deleteById(Long id);

    boolean exist(Long id);
    
    Collection<InmuebleDto> findBySociosId(final Long id);
    
    void addSocio2Inmueble(final UsuarioDto usuarioDto, final Long inmuebleId);
}
