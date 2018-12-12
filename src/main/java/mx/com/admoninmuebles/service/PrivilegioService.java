package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.PrivilegioDto;
import mx.com.admoninmuebles.persistence.model.Privilegio;

public interface PrivilegioService {
    Privilegio save(PrivilegioDto privilegioDto);
}
