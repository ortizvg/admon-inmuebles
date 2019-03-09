package mx.com.admoninmuebles.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.LocaleConst;
import mx.com.admoninmuebles.dto.GraficaDonaMorrisDataDto;
import mx.com.admoninmuebles.dto.GraficaDonaMorrisDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.ReporteTicketDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.repository.TicketRepository;

@Service
public class GraficoDonaTicketServiceImpl implements GraficoDonaTicketService {
	
	@Autowired
	private InmuebleService inmuebleService;
	
	@Autowired
	private TicketRepository TicketRepositort;
	
    @Autowired
    private MessageSource messages;
	

	@Override
	public GraficaDonaMorrisDto generarGraficaDona( Long inmuebleId,  String tipoTicketId ) {
		final String ESTATUS_TICKET_COLOR_ABIERTO = "#55ce63";
		final String ESTATUS_TICKET_COLOR_EN_PROCESO = "#ffbc34";
		final String ESTATUS_TICKET_COLOR_CERRADO = "#6c757d";
		final String IDENTIFICADOR_TIPO_GRAFICA = "tickets-grafica-dona";
		
		ReporteTicketDto reporte = generarReporteTicketsPorInmuebleId( inmuebleId, tipoTicketId );
		
		List<String> coloresEstatusTickets = new ArrayList<> ();
		coloresEstatusTickets.add( ESTATUS_TICKET_COLOR_ABIERTO );
		coloresEstatusTickets.add( ESTATUS_TICKET_COLOR_EN_PROCESO );
		coloresEstatusTickets.add( ESTATUS_TICKET_COLOR_CERRADO );
		
		GraficaDonaMorrisDataDto pagosAtrasados = new GraficaDonaMorrisDataDto();
		pagosAtrasados.setLabel( messages.getMessage("tickets.grafica.estatus.abierto", null, LocaleConst.LOCALE_MX )  );
		pagosAtrasados.setValue( reporte.getTicketsAbiertosPorcentaje() );
		
		GraficaDonaMorrisDataDto pagosPendientes = new GraficaDonaMorrisDataDto();
		pagosPendientes.setLabel( messages.getMessage("tickets.grafica.estatus.en.proceso", null, LocaleConst.LOCALE_MX ) );
		pagosPendientes.setValue( reporte.getTicketsEnProcesoPorcentaje() );
		
		GraficaDonaMorrisDataDto pagosRealizados = new GraficaDonaMorrisDataDto();
		pagosRealizados.setLabel( messages.getMessage("tickets.grafica.estatus.cerrado", null, LocaleConst.LOCALE_MX ) );
		pagosRealizados.setValue( reporte.getTicketsCerradosPorcentaje() );
		
		List<GraficaDonaMorrisDataDto> data = new ArrayList<> ();
		data.add( pagosRealizados );
		data.add( pagosPendientes );
		data.add( pagosAtrasados );
		
		
		
		GraficaDonaMorrisDto graficaDonaMorrisDto = new GraficaDonaMorrisDto();
		graficaDonaMorrisDto.setElement( IDENTIFICADOR_TIPO_GRAFICA );
		graficaDonaMorrisDto.setResize( true );
		graficaDonaMorrisDto.setColors( coloresEstatusTickets );
		graficaDonaMorrisDto.setData( data );
		
		return graficaDonaMorrisDto;
	}

	@Override
	public ReporteTicketDto generarReporteTicketsPorInmuebleId(Long inmuebleId,  String tipoTicketId) {
		Long ticketsTotal = 0L;
		Long ticketsAbiertos = 0L;
		Long ticektsEnProceso = 0L;
		Long ticketsCerrados = 0L;
		
		InmuebleDto inmuebleDto = inmuebleService.findById( inmuebleId );
		
		CompletableFuture<Long> ticketsTotalFuture  = CompletableFuture.supplyAsync(() ->  TicketRepositort.countByInmuebleId( inmuebleId ));
//		CompletableFuture<Long> ticketsAbiertosFuture   = CompletableFuture.supplyAsync(() -> TicketRepositort.countByInmuebleIdAndTipoTicketNameAndEstatusTicketName( inmuebleId, tipoTicketId, EstatusTicket.ABIERTO ) );
//		CompletableFuture<Long> ticektsEnProcesoFuture   = CompletableFuture.supplyAsync(() -> TicketRepositort.countByInmuebleIdAndTipoTicketNameAndEstatusTicketName( inmuebleId, tipoTicketId, EstatusTicket.EN_PROCESO ) );
//		CompletableFuture<Long> ticketsCerradosFuture   = CompletableFuture.supplyAsync(() ->  TicketRepositort.countByInmuebleIdAndTipoTicketNameAndEstatusTicketName( inmuebleId, tipoTicketId, EstatusTicket.CERRADO ) );
		CompletableFuture<Long> ticketsAbiertosFuture   = CompletableFuture.supplyAsync(() -> TicketRepositort.countByInmuebleIdAndTipoTicketNameAndEstatusTicketName( inmuebleId, tipoTicketId, "ABIETO" ) );
		CompletableFuture<Long> ticektsEnProcesoFuture   = CompletableFuture.supplyAsync(() -> TicketRepositort.countByInmuebleIdAndTipoTicketNameAndEstatusTicketName( inmuebleId, tipoTicketId, "EN_PROCESO" ) );
		CompletableFuture<Long> ticketsCerradosFuture   = CompletableFuture.supplyAsync(() ->  TicketRepositort.countByInmuebleIdAndTipoTicketNameAndEstatusTicketName( inmuebleId, tipoTicketId, "CERRADO" ) );
		CompletableFuture<Void> combinedFuture = CompletableFuture.allOf( ticketsTotalFuture, ticketsAbiertosFuture, ticektsEnProcesoFuture, ticketsCerradosFuture );
		 
		try {
			combinedFuture.get();
			ticketsTotal = ticketsTotalFuture.get();
			ticketsAbiertos = ticketsAbiertosFuture.get();
			ticektsEnProceso = ticektsEnProcesoFuture.get();
			ticketsCerrados = ticketsCerradosFuture.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		if( ticketsTotal == null || ticketsTotal == 0 ) {
			throw new BusinessException("tickets.inmueble.ticket.vacio");
		}
		
		ReporteTicketDto reporteTicketDto = new ReporteTicketDto();
		reporteTicketDto.setInmuebleId( inmuebleId );
		reporteTicketDto.setInmubleNombre( inmuebleDto.getNombre() );
		reporteTicketDto.setInmubleImagenUrl( inmuebleDto.getImagenUrl() );
		reporteTicketDto.setAdminBiId( inmuebleDto.getAdminBiId() );
		reporteTicketDto.setAdminBiNombre( inmuebleDto.getAdminBiNombre() );
		reporteTicketDto.setAdminBiApellidoPaterno( inmuebleDto.getAdminBiApellidoPaterno() );
		reporteTicketDto.setAdminBiApellidoMaterno( inmuebleDto.getAdminBiApellidoMaterno() );
		reporteTicketDto.setTicketsAbiertos( ticketsAbiertos );
		reporteTicketDto.setTicketsEnProceso( ticektsEnProceso );
		reporteTicketDto.setTicketsCerrados( ticketsCerrados );
		reporteTicketDto.setTicketsTotal( ticketsTotal );
		
		Double ticketsAbiertosPorcentaje = ( ticketsAbiertos  * 100.0 ) / ticketsTotal;
		Double ticektsEnProcesoPorcentaje = ( ticektsEnProceso  * 100.0 ) / ticketsTotal;
		Double ticketsCerradosPorcentaje = ( ticketsCerrados  * 100.0 ) / ticketsTotal;
		
		reporteTicketDto.setTicketsAbiertosPorcentaje( BigDecimal.valueOf( ticketsAbiertosPorcentaje ).setScale(2, RoundingMode.HALF_EVEN) );
		reporteTicketDto.setTicketsEnProcesoPorcentaje( BigDecimal.valueOf( ticektsEnProcesoPorcentaje ).setScale(2, RoundingMode.HALF_EVEN));
		reporteTicketDto.setTicketsCerradosPorcentaje( BigDecimal.valueOf( ticketsCerradosPorcentaje ).setScale(2, RoundingMode.HALF_EVEN) );
		
		return reporteTicketDto;
	}

}
