package mx.com.admoninmuebles.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.EstatusPagoConst;
import mx.com.admoninmuebles.constant.LocaleConst;
import mx.com.admoninmuebles.dto.GraficaDonaMorrisDataDto;
import mx.com.admoninmuebles.dto.GraficaDonaMorrisDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.NotificacionDto;
import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.dto.ReporteInmuebleMorososDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.EstatusPago;
import mx.com.admoninmuebles.persistence.model.TipoPago;

@Service
public class MorosoServiceImpl implements MorosoService {
	
	@Autowired
	private PagoService pagoService;
	
	@Autowired
	private InmuebleService inmuebleService;
	
	@Autowired
	private NotificacionService notificacionService;
	
    @Autowired
    private MessageSource messages;

	@Override
	public ReporteInmuebleMorososDto generarReporteMorososPorInmuebleId(final Long inmuebleId) {
		
		Long pagosTotal = 0L;
		Long pagosAtrasados = 0L;
		Long pagosRealizados = 0L;
		Long pagosVerificacion = 0L;
		Long pagosPendientes = 0L;
		
		InmuebleDto inmuebleDto = inmuebleService.findById( inmuebleId );
		
		CompletableFuture<Long> pagosTotalFuture  = CompletableFuture.supplyAsync(() ->  pagoService.getTotalPagosPorInmueble( inmuebleId ));
		CompletableFuture<Long> pagosAtrasadosFuture   = CompletableFuture.supplyAsync(() -> pagoService.getTotalPagosPorInmuebleYEstatusPagoNombreYTipoPagoNombre( inmuebleId, EstatusPago.ATRASADO, TipoPago.CUOTA ) );
		CompletableFuture<Long> pagosRealizadosFuture   = CompletableFuture.supplyAsync(() -> pagoService.getTotalPagosPorInmuebleYEstatusPagoNombreYTipoPagoNombre( inmuebleId, EstatusPago.PAGADO, TipoPago.CUOTA ) );
		CompletableFuture<Long> pagosVerificacionFuture   = CompletableFuture.supplyAsync(() ->  pagoService.getTotalPagosPorInmuebleYEstatusPagoNombreYTipoPagoNombre( inmuebleId, EstatusPago.VERIFICACION , TipoPago.CUOTA) );
		CompletableFuture<Long> pagosPendientesFuture   = CompletableFuture.supplyAsync(() -> pagoService.getTotalPagosPorInmuebleYEstatusPagoNombreYTipoPagoNombre( inmuebleId, EstatusPago.CERCANO, TipoPago.CUOTA ) );
		CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(pagosTotalFuture, pagosAtrasadosFuture, pagosRealizadosFuture, pagosVerificacionFuture, pagosPendientesFuture);
		 
		try {
			combinedFuture.get();
			pagosTotal = pagosTotalFuture.get();
			pagosAtrasados = pagosAtrasadosFuture.get();
			pagosRealizados = pagosRealizadosFuture.get();
			pagosVerificacion = pagosVerificacionFuture.get();
			pagosPendientes = pagosPendientesFuture.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		if( pagosTotal == null || pagosTotal == 0 ) {
			throw new BusinessException("morosos.inmueble.pagos.vacio");
		}
		
		ReporteInmuebleMorososDto reporteMorososPorInmueble = new ReporteInmuebleMorososDto();
		reporteMorososPorInmueble.setInmuebleId( inmuebleId );
		reporteMorososPorInmueble.setInmubleNombre( inmuebleDto.getNombre() );
		reporteMorososPorInmueble.setInmubleImagenUrl( inmuebleDto.getImagenUrl() );
		reporteMorososPorInmueble.setAdminBiId( inmuebleDto.getAdminBiId() );
		reporteMorososPorInmueble.setAdminBiNombre( inmuebleDto.getAdminBiNombre() );
		reporteMorososPorInmueble.setAdminBiApellidoPaterno( inmuebleDto.getAdminBiApellidoPaterno() );
		reporteMorososPorInmueble.setAdminBiApellidoMaterno( inmuebleDto.getAdminBiApellidoMaterno() );
		reporteMorososPorInmueble.setPagosAtrasados( pagosAtrasados );
		reporteMorososPorInmueble.setPagosPendientes( pagosPendientes );
		reporteMorososPorInmueble.setPagosRealizados( pagosRealizados );
		reporteMorososPorInmueble.setPagosVerificacion( pagosVerificacion );
		reporteMorososPorInmueble.setPagosTotal( pagosTotal );
		
		Double pagosRealizadorPorcenaje = ( pagosRealizados  * 100.0 ) / pagosTotal;
		Double pagosPendientePorcenaje = ( pagosPendientes  * 100.0 ) / pagosTotal;
		Double pagosAtrasadosPorcenaje = ( pagosAtrasados  * 100.0 ) / pagosTotal;
		Double pagosVerificacionPorcenaje = ( pagosVerificacion  * 100.0 ) / pagosTotal;
		
		reporteMorososPorInmueble.setPagosRealizadosPorcentaje( BigDecimal.valueOf( pagosRealizadorPorcenaje ).setScale(2, RoundingMode.HALF_EVEN) );
		reporteMorososPorInmueble.setPagosPendientesPorcentaje( BigDecimal.valueOf( pagosPendientePorcenaje ).setScale(2, RoundingMode.HALF_EVEN));
		reporteMorososPorInmueble.setPagosAtrasadosPorcentaje( BigDecimal.valueOf( pagosAtrasadosPorcenaje ).setScale(2, RoundingMode.HALF_EVEN) );
		reporteMorososPorInmueble.setPagosVerificacionPorcentaje( BigDecimal.valueOf( pagosVerificacionPorcenaje ).setScale(2, RoundingMode.HALF_EVEN) );
		
		return reporteMorososPorInmueble;
	}
	
	@Override
	public GraficaDonaMorrisDto generarGraficaDonaPorInmuebleId( Long inmuebleId ) {
		
		final String IDENTIFICADOR_TIPO_GRAFICA = "pagos-grafica-dona";
		
		ReporteInmuebleMorososDto reporte = generarReporteMorososPorInmuebleId( inmuebleId );
		
		List<String> coloresEstatusPagos = new ArrayList<> ();
		coloresEstatusPagos.add( EstatusPagoConst.ESTATUS_PAGO_COLOR_PAGADO );
		coloresEstatusPagos.add( EstatusPagoConst.ESTATUS_PAGO_COLOR_CERCANO );
		coloresEstatusPagos.add( EstatusPagoConst.ESTATUS_PAGO_COLOR_VERIFICACION );
		coloresEstatusPagos.add( EstatusPagoConst.ESTATUS_PAGO_COLOR_ATRASADO );
		
		GraficaDonaMorrisDataDto pagosAtrasados = new GraficaDonaMorrisDataDto();
		pagosAtrasados.setLabel( messages.getMessage("mororos.tablero.pago.atrasado", null, LocaleConst.LOCALE_MX )  );
		pagosAtrasados.setValue( reporte.getPagosAtrasadosPorcentaje() );
		
		GraficaDonaMorrisDataDto pagosPendientes = new GraficaDonaMorrisDataDto();
		pagosPendientes.setLabel(  messages.getMessage("mororos.tablero.pago.pendiente", null, LocaleConst.LOCALE_MX ) );
		pagosPendientes.setValue( reporte.getPagosPendientesPorcentaje() );
		
		GraficaDonaMorrisDataDto pagosRealizados = new GraficaDonaMorrisDataDto();
		pagosRealizados.setLabel( messages.getMessage("mororos.tablero.pago.pagado", null, LocaleConst.LOCALE_MX ) );
		pagosRealizados.setValue( reporte.getPagosRealizadosPorcentaje() );
		
		GraficaDonaMorrisDataDto pagosVerificacion = new GraficaDonaMorrisDataDto();
		pagosVerificacion.setLabel( messages.getMessage("mororos.tablero.pago.verificando" , null, LocaleConst.LOCALE_MX ) );
		pagosVerificacion.setValue( reporte.getPagosVerificacionPorcentaje() );
		
		List<GraficaDonaMorrisDataDto> data = new ArrayList<> ();
		data.add( pagosRealizados );
		data.add( pagosPendientes );
		data.add( pagosVerificacion );
		data.add( pagosAtrasados );
		
		
		
		GraficaDonaMorrisDto graficaDonaMorrisDto = new GraficaDonaMorrisDto();
		graficaDonaMorrisDto.setElement( IDENTIFICADOR_TIPO_GRAFICA );
		graficaDonaMorrisDto.setResize( true );
		graficaDonaMorrisDto.setColors( coloresEstatusPagos );
		graficaDonaMorrisDto.setData( data );
		
		return graficaDonaMorrisDto;
	}
	
	@Override
	public void enviarNotificacionRecordatorioPago( Long pagoId ) {
		
		final Long DIAS_PARA_VENCIMIENTO_PAGO = 5L;
		
		final Long DIAS_EXPOSICION_NOTIFICACION = 5L;
		
		PagoDto pago = pagoService.buscarId( pagoId );
		
		NotificacionDto notificacionDto = new NotificacionDto();
		notificacionDto.setFechaExposicionInicial( LocalDate.now() );
		notificacionDto.setFechaExposicionFinal( LocalDate.now().plusDays( DIAS_EXPOSICION_NOTIFICACION ) );
		notificacionDto.setUsuarioId( pago.getUsuarioId() );
		notificacionDto.setTitulo( messages.getMessage("morosos.notificacion.recordatorio.pago.titulo" , null, LocaleConst.LOCALE_MX ) );
		
		LocalDateTime fechavencimientoPago = pago.getFechaCreacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays( DIAS_PARA_VENCIMIENTO_PAGO );
//		LocalDateTime fechavencimientoPago = pago.getFechaCreacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime fechaActual =  LocalDateTime.now();
		
		Long diasAtraso = fechavencimientoPago.until(fechaActual, ChronoUnit.DAYS );
		
		StringBuffer notificacionDesc = new StringBuffer(  messages.getMessage("morosos.notificacion.recordatorio.pago.descripcion" , null, LocaleConst.LOCALE_MX )  );
		notificacionDesc.append("\n");
		notificacionDesc.append( messages.getMessage("morosos.notificacion.recordatorio.pago.concepto" , null, LocaleConst.LOCALE_MX ) ).append(" ").append( pago.getConcepto() );
		notificacionDesc.append("\n");
		notificacionDesc.append(messages.getMessage("morosos.notificacion.recordatorio.pago.monto" , null, LocaleConst.LOCALE_MX ) ).append(" ").append( pago.getMonto() );
		notificacionDesc.append("\n");
		notificacionDesc.append(messages.getMessage("morosos.notificacion.recordatorio.pago.atraso.dias" , null, LocaleConst.LOCALE_MX ) ).append(" ").append( diasAtraso );
		
		notificacionDto.setDescripcion( notificacionDesc.toString() );
		
		
		notificacionService.save(notificacionDto);
	}

}
