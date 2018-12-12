package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.TipoTelefonoDto;
import mx.com.admoninmuebles.persistence.model.TipoTelefono;

public interface TipoTelefonoService {
    TipoTelefono save(TipoTelefonoDto tipoTelefonoDto);
}
