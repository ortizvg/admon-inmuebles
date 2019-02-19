package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Locale;

import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.dto.PagoPaypalDto;
import mx.com.admoninmuebles.dto.PagoTarjetaDto;

public interface PagoService {
	
	void generarPagosMensuales();
	PagoDto generarPagosPorSocio(PagoDto pagoDto);
	void actualizarEstatusPagosMensuales();
	void generarPagos(PagoDto pagoDto);
	void eliminarPorId(Long idPago);
	boolean existePago(Long idPago);
	PagoDto pagarTranferenciaBancaria(final PagoDto pagoDto, Locale locale);
	PagoDto pagarPaypal(final PagoPaypalDto pagoPaypalDto);
	PagoDto pagarTarjeta(final PagoTarjetaDto pagoTarjetaDto);
    PagoDto guardar(final PagoDto pagoDto);
    PagoDto verificar(final Long idPago);
    PagoDto buscarId(final Long idPago);
    Collection<PagoDto> buscarTodo();
    Collection<PagoDto> buscarTodoPorEstatus( Long idEstatus);
    Collection<PagoDto> buscarPorUsuario(final Long idUsuario);
    Collection<PagoDto> buscarPorInmueble(final Long idInmueble);
    Collection<PagoDto> buscarPorCodigoZona(final String codigoZona );
    Collection<PagoDto> buscarPorContador(Long idContador);
    Collection<PagoDto> buscarPorAdminBi(Long idAdminBi);
    Collection<PagoDto> buscarPorAdminZona(Long idAdminZona);
    
    
    Collection<PagoDto> buscarPorInmuebleYEstatusPagoNombre(final Long idInmueble, final String nombre);
    Long getTotalPagosPorInmueble(final Long idInmueble);
    Long getTotalPagosPorInmuebleYEstatusPagoNOmbre(final Long idInmueble, final String nombre);
    
    Collection<PagoDto> buscarPorSocioYEstatusPagoNombre(final Long idSocio, final String nombre);
    
}
