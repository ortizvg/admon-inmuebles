package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.dto.PagoPaypalDto;
import mx.com.admoninmuebles.dto.PagoTarjetaDto;
import mx.com.admoninmuebles.dto.PagoTransferenciaBancariaDto;

public interface PagoService {
	
	void generarPagosMensuales();
	void actualizarEstatusPagosMensuales();
	boolean existePago(Long idPago);
	PagoDto pagarTranferenciaBancaria(final PagoDto pagoDto);
	PagoDto pagarPaypal(final PagoPaypalDto pagoPaypalDto);
	PagoDto pagarTarjeta(final PagoTarjetaDto pagoTarjetaDto);
    PagoDto guardar(final PagoDto pagoDto);
    PagoDto verificar(final Long idPago);
    PagoDto buscarId(final Long idPago);
    Collection<PagoDto> buscarTodo();
    Collection<PagoDto> buscarPorUsuario(final Long idUsuario);
    Collection<PagoDto> buscarPorInmueble(final Long idInmueble);
    Collection<PagoDto> buscarPorCodigoZona(final String codigoZona );
    
}
