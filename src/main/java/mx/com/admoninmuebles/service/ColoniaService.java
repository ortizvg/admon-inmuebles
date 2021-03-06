package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ColoniaDto;

public interface ColoniaService {

    void save(ColoniaDto coloniaDto);
    
    void deleteById(final Long codigo);
    
    boolean isRegistrada(final Long id);

    Collection<ColoniaDto> findAll();

    ColoniaDto findById(Long id);

    Collection<ColoniaDto> findByZonaIsNotNull();

    Collection<ColoniaDto> findBycodigoPostal(String codigoPostal);
    
    Collection<ColoniaDto> findBycodigoPostalAndEstadoId(final String codigoPostal, final Long estadoId);
    
    Collection<ColoniaDto> findByZonaCodigo(String zonaCodigo);
    
    Collection<ColoniaDto> findBycodigoPostalAndZonaCodigo(final String codigoPostal, String zonaCodigo);
    
    Collection<ColoniaDto> findByAdminZona(Long adminZonaId);
    
    boolean isFiltro( ColoniaDto coloniaDto );
    Collection<ColoniaDto> filtrar( ColoniaDto coloniaDto );

}
