package mx.com.admoninmuebles.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.ReglamentoDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Archivo;
import mx.com.admoninmuebles.persistence.model.Reglamento;
import mx.com.admoninmuebles.persistence.repository.ArchivoRepository;
import mx.com.admoninmuebles.persistence.repository.ReglamentoRepository;

@Service
public class ReglamentoServiceImpl implements ReglamentoService {
	
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
    			
    			Archivo archivo = new Archivo();
    			archivo.setId(UUID.randomUUID().toString());
    			archivo.setBytes(reglamentoDto.getArchivoMP().getBytes());
    			archivo.setNombre(reglamentoDto.getArchivoMP().getOriginalFilename());
    			archivo.setTipoContenido(reglamentoDto.getArchivoMP().getContentType());
    			archivoCreado = archivoRepository.save(archivo);
    			
    		} catch (IOException e) {
    			throw new BusinessException("pago.error.carga.archivo");
    		}
    	}
		
		Reglamento reglamento = modelMapper.map(reglamentoDto, Reglamento.class);
		reglamento.setArchivo(archivoCreado);
		
		reglamento = reglamentoRepository.save(reglamento);
		
		return modelMapper.map(reglamento, ReglamentoDto.class);
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

}
