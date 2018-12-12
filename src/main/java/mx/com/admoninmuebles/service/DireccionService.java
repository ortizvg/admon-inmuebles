package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.DireccionDto;
import mx.com.admoninmuebles.persistence.model.Direccion;

public interface DireccionService {
    Direccion save(DireccionDto direccionDto);
}
