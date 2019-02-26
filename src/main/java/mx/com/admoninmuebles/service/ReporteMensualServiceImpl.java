package mx.com.admoninmuebles.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.ComunConst;
import mx.com.admoninmuebles.dto.ReporteMensualDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Archivo;
import mx.com.admoninmuebles.persistence.model.ReporteMensual;
import mx.com.admoninmuebles.persistence.model.TipoReporteMensual;
import mx.com.admoninmuebles.persistence.repository.ArchivoRepository;
import mx.com.admoninmuebles.persistence.repository.ReporteMensualRepository;
import mx.com.admoninmuebles.persistence.repository.TipoReporteMensualRepository;

@Service
public class ReporteMensualServiceImpl implements ReporteMensualService {
	
	Logger logger = LoggerFactory.getLogger(ReporteMensualServiceImpl.class);
	
	@Autowired
	private ReporteMensualRepository reporteMensualRepository;
	
	@Autowired
	private ArchivoRepository archivoRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private TipoReporteMensualRepository tipoReporteMensualRepository;

	@Transactional
	@Override
	public ReporteMensualDto guardar(final ReporteMensualDto reporteMensualDto) {
		
		if( existeReporte( reporteMensualDto ) ) {
			throw new BusinessException("reporte.mensual.validacion.existe");
		}
		
		
		Archivo archivoReporteCreado = null;
    	if(  reporteMensualDto.getReporteArchivo() != null ) {
    		try {
    			validarArhivoReporteMansual( reporteMensualDto );
    			
    			Archivo archivo = new Archivo();
    			archivo.setId(UUID.randomUUID().toString());
    			archivo.setBytes(reporteMensualDto.getReporteArchivo().getBytes());
    			archivo.setNombre(reporteMensualDto.getReporteArchivo().getOriginalFilename());
    			archivo.setTipoContenido(reporteMensualDto.getReporteArchivo().getContentType());
    			archivoReporteCreado = archivoRepository.save(archivo);
    			
    		} catch (IOException e) {
    			logger.error("Error al guardar el archivo para el reporte mensual: ", e);
    			throw new BusinessException("reporte.mensual.archivo.guardado.error");
    		}
    	}
		
		ReporteMensual reporteMensual = modelMapper.map(reporteMensualDto, ReporteMensual.class);
		reporteMensual.setArchivo(archivoReporteCreado);
		
		reporteMensual = reporteMensualRepository.save(reporteMensual);
		
		return modelMapper.map(reporteMensual, ReporteMensualDto.class);
	}
	
	private void validarArhivoReporteMansual(ReporteMensualDto reporteMensualDto) {
		if( !MediaType.APPLICATION_PDF_VALUE.equalsIgnoreCase( reporteMensualDto.getReporteArchivo().getContentType() ) ) {
			throw new BusinessException("reporte.mensual.archivo.validacion.mediatype.pdf");
		}
		
		if( reporteMensualDto.getReporteArchivo().getSize() > ComunConst.TAMANIO_2_MB ) {
			throw new BusinessException("reporte.mensual.archivo.validacion.tamanio");
		}
	}
	
	private boolean existeReporte(final ReporteMensualDto reporteMensualDto) {
		Optional<ReporteMensual> tipoReporteMensualOpt = reporteMensualRepository.findByInmuebleIdAndAnioAndMesIdAndTipoReporteMensualId(reporteMensualDto.getInmuebleId(), reporteMensualDto.getAnio(), reporteMensualDto.getMesId(), reporteMensualDto.getTipoReporteMensualId());
		return tipoReporteMensualOpt.isPresent();
	}

	@Override
	public ReporteMensualDto actualizar(final ReporteMensualDto reporteMensualDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public void eliminar(final Long idReporte) {
		Optional<ReporteMensual> reporteMensualOpt = reporteMensualRepository.findById( idReporte );
		
		if( !reporteMensualOpt.isPresent() ) {
			throw new BusinessException("reporte.mensual.no.existe");
		}
		
		reporteMensualRepository.deleteById(reporteMensualOpt.get().getId());
		
	}

	@Override
	public ReporteMensualDto buscarPorId(final Long idReporte) {
		Optional<ReporteMensual> reporteMensualOpt = reporteMensualRepository.findById( idReporte );
		
		if( !reporteMensualOpt.isPresent() ) {
			throw new BusinessException("reporte.mensual.no.existe");
		}
		
		return modelMapper.map( reporteMensualOpt.get(), ReporteMensualDto.class ); 
	}

	@Override
	public Collection<ReporteMensualDto> buscarPorInmuebleId(final Long idInmueble) {
		return StreamSupport.stream(reporteMensualRepository.findByInmuebleId( idInmueble ).spliterator(), false)
				.map(reporteMensual -> modelMapper.map(reporteMensual, ReporteMensualDto.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<ReporteMensualDto> buscarPorContadorId(final Long idContador) {
		return StreamSupport.stream(reporteMensualRepository.findByContadorId( idContador ).spliterator(), false)
				.map(reporteMensual -> modelMapper.map(reporteMensual, ReporteMensualDto.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<ReporteMensualDto> buscarReciente5PorSocioId(final Long idInmueble) {
		
		return StreamSupport.stream(reporteMensualRepository.findFirst5ByInmuebleIdOrderByIdDesc( idInmueble ).spliterator(), false)
				.map(reporteMensual -> modelMapper.map(reporteMensual, ReporteMensualDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<ReporteMensualDto> buscarActualPorInmuebleId(Long idInmueble) {
		Integer anioActual = LocalDate.now().getYear();
		Long mes =  Long.valueOf( LocalDate.now().getMonthValue() - 1 );
		if( LocalDate.now().getMonthValue()  == 1 ) {
			anioActual = anioActual - 1;
		}
		
		Collection<TipoReporteMensual> tiposReporteMensual =  StreamSupport.stream(tipoReporteMensualRepository.findAll().spliterator(), false).collect(Collectors.toList());
		
		return StreamSupport.stream(reporteMensualRepository.findByInmuebleIdAndAnioAndMesIdAndTipoReporteMensualIn(idInmueble, anioActual, mes, tiposReporteMensual ).spliterator(), false)
				.map(reporteMensual -> modelMapper.map(reporteMensual, ReporteMensualDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<ReporteMensualDto> buscarActualPorContadorId(Long idContador) {
		Integer anioActual = LocalDate.now().getYear();
		Long mes =  Long.valueOf( LocalDate.now().getMonthValue() - 1 );
		if( LocalDate.now().getMonthValue()  == 1 ) {
			anioActual = anioActual - 1;
		}
		
		Collection<TipoReporteMensual> tiposReporteMensual =  StreamSupport.stream(tipoReporteMensualRepository.findAll().spliterator(), false).collect(Collectors.toList());
		
		return StreamSupport.stream(reporteMensualRepository.findByInmuebleContadorIdAndAnioAndMesIdAndTipoReporteMensualIn(idContador, anioActual, mes, tiposReporteMensual ).spliterator(), false)
				.map(reporteMensual -> modelMapper.map(reporteMensual, ReporteMensualDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<ReporteMensualDto> buscarPorAdminBiId(Long idAdminBi) {
		return StreamSupport.stream(reporteMensualRepository.findByAdminBiId( idAdminBi ).spliterator(), false)
				.map(reporteMensual -> modelMapper.map(reporteMensual, ReporteMensualDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<ReporteMensualDto> buscarPorAdminZonaId(Long idAdminZona) {
		return StreamSupport.stream(reporteMensualRepository.findByAdminZonaId( idAdminZona ).spliterator(), false)
				.map(reporteMensual -> modelMapper.map(reporteMensual, ReporteMensualDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<ReporteMensualDto> buscarTodo() {
		return StreamSupport.stream(reporteMensualRepository.findAll().spliterator(), false)
				.map(reporteMensual -> modelMapper.map(reporteMensual, ReporteMensualDto.class))
				.collect(Collectors.toList());
	}
	
}
