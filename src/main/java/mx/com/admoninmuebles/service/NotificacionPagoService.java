package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.PagoDto;

public interface NotificacionPagoService {
	
	void notificarMorosos();
	void notificarPagoRealizado(final PagoDto pagoDto);
	void notificarVerificacionPago(final PagoDto pagoDto);
	void notificarGeneracionPago(PagoDto pagoDto);

}
