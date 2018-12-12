package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.AreaServicioDto;
import mx.com.admoninmuebles.persistence.model.AreaServicio;

public interface AreaServicioService {
    AreaServicio save(AreaServicioDto areaServicioDto);

    Collection<AreaServicioDto> findAll();

    AreaServicioDto findById(Long id);

    void delete(Long id);
}
