package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.TipoPagoBancarioDto;
import mx.com.admoninmuebles.persistence.model.TipoPagoBancario;

public interface TipoPagoBancarioService {
    TipoPagoBancario save(TipoPagoBancarioDto tipoPagoBancarioDto);
    Collection<TipoPagoBancarioDto> findAll();
    TipoPagoBancarioDto findById(Long idTipoPagoBancario);
    void deleteById(Long idTipoPagoBancario);
}
