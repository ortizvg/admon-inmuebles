package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.TipoPagoDto;
import mx.com.admoninmuebles.persistence.model.TipoPago;

public interface TipoPagoService {
    TipoPago save(TipoPagoDto tipoPagoDto);
    Collection<TipoPagoDto> findAll();
    Collection<TipoPagoDto> findAllByLang(String lang);
    TipoPagoDto findById(Long idTipoPago);
    void deleteById(Long idTipoPago);
}
