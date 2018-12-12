package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.TelefonoDto;
import mx.com.admoninmuebles.persistence.model.Telefono;

public interface TelefonoService {
    Telefono save(TelefonoDto telefonoDto);
}
