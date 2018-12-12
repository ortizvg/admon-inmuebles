package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ColoniaDto;

public interface ColoniaService {

    void save(ColoniaDto coloniaDto);
    
    void deleteById(final Long codigo);

    Collection<ColoniaDto> findAll();

    ColoniaDto findById(Long id);

    Collection<ColoniaDto> findByZonaIsNotNull();

    Collection<ColoniaDto> findBycodigoPostal(String codigoPostal);
    
    Collection<ColoniaDto> findByZonaCodigo(String zonaCodigo);
    
    Collection<ColoniaDto> findBycodigoPostalAndZonaCodigo(final String codigoPostal, String zonaCodigo);

}
