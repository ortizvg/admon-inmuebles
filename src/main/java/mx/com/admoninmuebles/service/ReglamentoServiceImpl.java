package mx.com.admoninmuebles.service;

import java.io.IOException;
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
import mx.com.admoninmuebles.dto.ReglamentoDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Archivo;
import mx.com.admoninmuebles.persistence.model.Reglamento;
import mx.com.admoninmuebles.persistence.repository.ArchivoRepository;
import mx.com.admoninmuebles.persistence.repository.ReglamentoRepository;

@Service
public class ReglamentoServiceImpl implements ReglamentoService {
	
	Logger logger = LoggerFactory.getLogger(ReglamentoServiceImpl.class);
	
	@Autowired
	private ReglamentoRepository reglamentoRepository;
	
	@Autowired
	private ArchivoRepository archivoRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Transactional
	@Override
	public ReglamentoDto guardar(ReglamentoDto reglamentoDto) {
		
		Archivo archivoCreado = null;
    	if(  reglamentoDto.getArchivoMP() != null ) {
    		try {
    			
    			validarArhivoReglamento( reglamentoDto );
    			
    			Archivo archivo = new Archivo();
    			archivo.setId(UUID.randomUUID().toString());
    			archivo.setBytes(reglamentoDto.getArchivoMP().getBytes());
    			archivo.setNombre(reglamentoDto.getArchivoMP().getOriginalFilename());
    			archivo.setTipoContenido(reglamentoDto.getArchivoMP().getContentType());
    			archivoCreado = archivoRepository.save(archivo);
    			
    		} catch (IOException e) {
    			logger.error("Error al guardar el archivo para el reglamento: ", e);
    			throw new BusinessException("reglamentos.archivo.guardado.error");
    		}
    	}
		
		Reglamento reglamento = modelMapper.map(reglamentoDto, Reglamento.class);
		reglamento.setArchivo(archivoCreado);
		
		reglamento = reglamentoRepository.save(reglamento);
		
		return modelMapper.map(reglamento, ReglamentoDto.class);
	}
	
	private void validarArhivoReglamento(ReglamentoDto reglamentoDto) {
		if( !MediaType.APPLICATION_PDF_VALUE.equalsIgnoreCase( reglamentoDto.getArchivoMP().getContentType() ) ) {
			throw new BusinessException("reglamentos.archivo.validacion.mediatype.pdf");
		}
		
		if( reglamentoDto.getArchivoMP().getSize() > ComunConst.TAMANIO_2_MB ) {
			throw new BusinessException("reglamentos.archivo.validacion.tamanio");
		}
	}

	@Override
	public ReglamentoDto actualizar(ReglamentoDto reglamentoDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public void eliminar(Long idReglamento) {
		Optional<Reglamento> reglamentoOpt = reglamentoRepository.findById( idReglamento );
		
		if( !reglamentoOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		
		reglamentoRepository.deleteById(reglamentoOpt.get().getId());
		
	}

	@Override
	public ReglamentoDto buscarPorId(Long idReglamento) {
		Optional<Reglamento> reglamentoOpt = reglamentoRepository.findById( idReglamento );
		
		if( !reglamentoOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		
		return modelMapper.map(reglamentoOpt.get(), ReglamentoDto.class);
	}

	@Override
	public Collection<ReglamentoDto> buscarPorInmuebleId(Long idInmueble) {
		return StreamSupport.stream(reglamentoRepository.findByInmuebleId( idInmueble ).spliterator(), false)
				.map(reglamento -> modelMapper.map(reglamento, ReglamentoDto.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<ReglamentoDto> buscarPorContadorId(Long idContador) {
		return StreamSupport.stream(reglamentoRepository.findByContadorId( idContador ).spliterator(), false)
				.map(reglamento -> modelMapper.map(reglamento, ReglamentoDto.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<ReglamentoDto> buscarPorAdminBiId(Long idAdminBi) {
		return StreamSupport.stream(reglamentoRepository.findByAdminBiId( idAdminBi ).spliterator(), false)
				.map(reglamento -> modelMapper.map(reglamento, ReglamentoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<ReglamentoDto> buscarPorAdminZonaId(Long idAdminZona) {
		return StreamSupport.stream(reglamentoRepository.findByAdminZonaId( idAdminZona ).spliterator(), false)
				.map(reglamento -> modelMapper.map(reglamento, ReglamentoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<ReglamentoDto> buscarTodo() {
		return StreamSupport.stream(reglamentoRepository.findAll().spliterator(), false)
				.map(reglamento -> modelMapper.map(reglamento, ReglamentoDto.class))
				.collect(Collectors.toList());
	}

}
