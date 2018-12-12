package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.DatosAdicionalesDto;
import mx.com.admoninmuebles.persistence.model.DatosAdicionales;

public interface DatosAdicionalesService {
    DatosAdicionales save(DatosAdicionalesDto datosAdicionalesDto);
}
