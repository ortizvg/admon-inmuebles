package mx.com.admoninmuebles.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.ColorConst;
import mx.com.admoninmuebles.constant.EstatusTicketConst;
import mx.com.admoninmuebles.constant.LocaleConst;
import mx.com.admoninmuebles.dto.GraficaDonaMorrisDataDto;
import mx.com.admoninmuebles.dto.GraficaDonaMorrisDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.ReporteTicketDto;
import mx.com.admoninmuebles.dto.TicketDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.repository.TicketRepository;

@Service
public class GraficoDonaTicketServiceImpl implements GraficoDonaTicketService {
	
	Logger logger = LoggerFactory.getLogger(GraficoDonaTicketServiceImpl.class);
	
	@Autowired
	private InmuebleService inmuebleService;
	
	@Autowired
	private TicketRepository ticketRepository;
	
    @Autowired
    private MessageSource messages;
	
    @Autowired
    private ModelMapper modelMapper;

	@Override
	public GraficaDonaMorrisDto generarGraficaDona( Long inmuebleId,  Long tipoTicketId ) {
		final String ESTATUS_TICKET_COLOR_ABIERTO = ColorConst.COLOR_INFO;
		final String ESTATUS_TICKET_COLOR_EN_PROCESO = ColorConst.COLOR_MUTED;
		final String ESTATUS_TICKET_COLOR_CERRADO = ColorConst.COLOR_SUCCESS;
		final String IDENTIFICADOR_TIPO_GRAFICA = "tickets-grafica-dona";
		
		ReporteTicketDto reporte = generarReporteTicketsPorInmuebleId( inmuebleId, tipoTicketId );
		
		List<String> coloresEstatusTickets = new ArrayList<> ();
		coloresEstatusTickets.add( ESTATUS_TICKET_COLOR_CERRADO );
		coloresEstatusTickets.add( ESTATUS_TICKET_COLOR_EN_PROCESO );
		coloresEstatusTickets.add( ESTATUS_TICKET_COLOR_ABIERTO );
		
		GraficaDonaMorrisDataDto ticketsAbietos = new GraficaDonaMorrisDataDto();
		ticketsAbietos.setLabel( messages.getMessage("ticket.grafico.estatus.abierto", null, LocaleConst.LOCALE_MX )  );
		ticketsAbietos.setValue( reporte.getTicketsAbiertosPorcentaje() );
		
		GraficaDonaMorrisDataDto ticketsEnProceso = new GraficaDonaMorrisDataDto();
		ticketsEnProceso.setLabel( messages.getMessage("ticket.grafico.estatus.enproceso", null, LocaleConst.LOCALE_MX ) );
		ticketsEnProceso.setValue( reporte.getTicketsEnProcesoPorcentaje() );
		
		GraficaDonaMorrisDataDto ticketsCerrados = new GraficaDonaMorrisDataDto();
		ticketsCerrados.setLabel( messages.getMessage("ticket.grafico.estatus.cerrado", null, LocaleConst.LOCALE_MX ) );
		ticketsCerrados.setValue( reporte.getTicketsCerradosPorcentaje() );
		
		List<GraficaDonaMorrisDataDto> data = new ArrayList<> ();
		data.add( ticketsCerrados );
		data.add( ticketsEnProceso );
		data.add( ticketsAbietos );
		
		
		
		GraficaDonaMorrisDto graficaDonaMorrisDto = new GraficaDonaMorrisDto();
		graficaDonaMorrisDto.setElement( IDENTIFICADOR_TIPO_GRAFICA );
		graficaDonaMorrisDto.setResize( true );
		graficaDonaMorrisDto.setColors( coloresEstatusTickets );
		graficaDonaMorrisDto.setData( data );
		
		return graficaDonaMorrisDto;
	}

	@Override
	public ReporteTicketDto generarReporteTicketsPorInmuebleId(Long inmuebleId,  Long tipoTicketId) {
		
		logger.info("REPORTE TICKET" );
		logger.info("inmuebleId: " + inmuebleId );
		logger.info("tipoTicketId: " +  tipoTicketId );
		
		Long ticketsTotal = 0L;
		Long ticketsAbiertos = 0L;
		Long ticektsEnProceso = 0L;
		Long ticketsCerrados = 0L;
		
		InmuebleDto inmuebleDto = inmuebleService.findById( inmuebleId );
		
		CompletableFuture<Long> ticketsTotalFuture  = CompletableFuture.supplyAsync(() ->  ticketRepository.countByInmuebleIdAndTipoTicketId( inmuebleId, tipoTicketId ));
		CompletableFuture<Long> ticketsAbiertosFuture   = CompletableFuture.supplyAsync(() -> ticketRepository.countByInmuebleIdAndTipoTicketIdAndEstatusTicketName( inmuebleId, tipoTicketId, EstatusTicketConst.ABIERTO ) );
		CompletableFuture<Long> ticektsEnProcesoFuture   = CompletableFuture.supplyAsync(() -> ticketRepository.countByInmuebleIdAndTipoTicketIdAndEstatusTicketName( inmuebleId, tipoTicketId, EstatusTicketConst.EN_PROCESO ) );
		CompletableFuture<Long> ticketsCerradosFuture   = CompletableFuture.supplyAsync(() ->  ticketRepository.countByInmuebleIdAndTipoTicketIdAndEstatusTicketName( inmuebleId, tipoTicketId, EstatusTicketConst.CERRADO ) );
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
			throw new BusinessException("ticket.grafico.tickets.cero");
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

	@Override
	public Collection<TicketDto> findByUsuarioAsignadoIdAndEstatus(Long usuarioAsignadoId, String estatus) {
		 return StreamSupport.stream(ticketRepository.findByUsuarioAsignadoIdAndEstatus(usuarioAsignadoId , estatus).spliterator(), false).map(ticket -> modelMapper.map(ticket, TicketDto.class)).collect(Collectors.toList());
	}

	@Override
	public Collection<TicketDto> filtrarTickets(Long inmuebleId, Long tipoTicketId, String estatus) {
		return StreamSupport.stream(ticketRepository.findByInmuebleIdAndTipoTicketNameAndEstatusTicketName(inmuebleId, tipoTicketId, estatus).spliterator(), false).map(ticket -> modelMapper.map(ticket, TicketDto.class)).collect(Collectors.toList());
	}
}
