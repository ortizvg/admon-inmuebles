package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.AvisoOportunoDto;
import mx.com.admoninmuebles.persistence.model.AvisoOportuno;

public interface AvisoOportunoService {
    AvisoOportuno save(AvisoOportunoDto avisoOportunoDto);
    Collection<AvisoOportunoDto> findAll();
    AvisoOportunoDto findById(Long idAvisoOportuno);
    void deleteById(Long idAvisoOportuno);
}
