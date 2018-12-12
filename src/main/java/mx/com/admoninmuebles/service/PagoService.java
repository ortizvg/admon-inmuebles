package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.persistence.model.Pago;

public interface PagoService {
    Pago save(PagoDto pagoDto);
}
