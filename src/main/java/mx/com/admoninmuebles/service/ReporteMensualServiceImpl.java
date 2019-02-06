package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.ReporteMensualDto;
import mx.com.admoninmuebles.dto.TipoReporteMensualDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Archivo;
import mx.com.admoninmuebles.persistence.model.ReporteMensual;
import mx.com.admoninmuebles.persistence.repository.ArchivoRepository;
import mx.com.admoninmuebles.persistence.repository.ReporteMensualRepository;

@Service
public class ReporteMensualServiceImpl implements ReporteMensualService {
	
	@Autowired
	private ReporteMensualRepository reporteMensualRepository;
	
	@Autowired
	private ArchivoRepository archivoRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Transactional
	@Override
	public ReporteMensualDto guardar(ReporteMensualDto reporteMensualDto) {
		
		Archivo archivoReporteCreado = null;
		Archivo archivo = new Archivo();
		archivo.setId(UUID.randomUUID().toString());
		archivo.setBytes(reporteMensualDto.getArchivoBytes());
		archivo.setNombre(reporteMensualDto.getArchivoNombre());
		archivo.setTipoContenido(reporteMensualDto.getArchivoTipoContenido());
		archivoReporteCreado = archivoRepository.save(archivo);
		
		ReporteMensual reporteMensual = modelMapper.map(reporteMensualDto, ReporteMensual.class);
		reporteMensual.setArchivo(archivoReporteCreado);
		
		reporteMensual = reporteMensualRepository.save(reporteMensual);
		
		return modelMapper.map(reporteMensual, ReporteMensualDto.class);
	}

	@Override
	public ReporteMensualDto actualizar(ReporteMensualDto reporteMensualDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public void eliminar(Long idReporte) {
		Optional<ReporteMensual> reporteMensualOpt = reporteMensualRepository.findById( idReporte );
		
		if( !reporteMensualOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		
		archivoRepository.deleteById(reporteMensualOpt.get().getArchivo().getId());
		reporteMensualRepository.deleteById(reporteMensualOpt.get().getId());
		
	}

	@Override
	public ReporteMensualDto buscarPorId(Long idReporte) {
		Optional<ReporteMensual> reporteMensualOpt = reporteMensualRepository.findById( idReporte );
		
		if( !reporteMensualOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		
		return modelMapper.map( reporteMensualOpt.get(), ReporteMensualDto.class ); 
	}

	@Override
	public Collection<ReporteMensualDto> buscarPorInmuebleId(Long idInmueble) {
		return StreamSupport.stream(reporteMensualRepository.findByInmuebleId( idInmueble ).spliterator(), false)
				.map(reporteMensual -> modelMapper.map(reporteMensual, ReporteMensualDto.class))
				.collect(Collectors.toList());
	}
	
}
