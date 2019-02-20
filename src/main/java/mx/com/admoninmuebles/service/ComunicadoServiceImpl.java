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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.ComunConst;
import mx.com.admoninmuebles.dto.ComunicadoDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Archivo;
import mx.com.admoninmuebles.persistence.model.Comunicado;
import mx.com.admoninmuebles.persistence.repository.ArchivoRepository;
import mx.com.admoninmuebles.persistence.repository.ComunicadoRepository;

@Service
public class ComunicadoServiceImpl implements ComunicadoService {
	
	@Autowired
	private ComunicadoRepository comunicadoRepository;
	
	@Autowired
	private ArchivoRepository archivoRepository;
	
	@Autowired
	private NotificacionReporteService notificacionReporteService;
	
	@Autowired
	private ModelMapper modelMapper;

	@Transactional
	@Override
	public ComunicadoDto guardar(ComunicadoDto comunicadoDto) {
		
		Archivo archivoCreado = null;
    	if(  comunicadoDto.getArchivoMP() != null ) {
    		try {
    			validarArhivoComunicado( comunicadoDto );
    			Archivo archivo = new Archivo();
    			archivo.setId(UUID.randomUUID().toString());
    			archivo.setBytes(comunicadoDto.getArchivoMP().getBytes());
    			archivo.setNombre(comunicadoDto.getArchivoMP().getOriginalFilename());
    			archivo.setTipoContenido(comunicadoDto.getArchivoMP().getContentType());
    			archivoCreado = archivoRepository.save(archivo);
    			
    			
    		} catch (IOException e) {
    			throw new BusinessException("comunicados.archivo.guardado.error");
    		}
    	}
		
		Comunicado comunicado = modelMapper.map(comunicadoDto, Comunicado.class);
		comunicado.setArchivo(archivoCreado);
		
		comunicado = comunicadoRepository.save(comunicado);
		
		notificacionReporteService.notificarComunicado( modelMapper.map(comunicado, ComunicadoDto.class) );
		
		return modelMapper.map(comunicado, ComunicadoDto.class);
	}
	
	private void validarArhivoComunicado(ComunicadoDto comunicadoDto) {
		if( !MediaType.APPLICATION_PDF_VALUE.equalsIgnoreCase( comunicadoDto.getArchivoMP().getContentType() ) ) {
			throw new BusinessException("comunicados.archivo.validacion.mediatype.pdf");
		}
		
		if( comunicadoDto.getArchivoMP().getSize() > ComunConst.TAMANIO_1_MB ) {
			throw new BusinessException("comunicados.archivo.validacion.tamanio");
		}
	}

	@Override
	public ComunicadoDto actualizar(ComunicadoDto comunicadoDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public void eliminar(Long idComunicado) {
		Optional<Comunicado> comunicadoOpt = comunicadoRepository.findById( idComunicado );
		
		if( !comunicadoOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		
		comunicadoRepository.deleteById(comunicadoOpt.get().getId());
		
	}

	@Override
	public ComunicadoDto buscarPorId(Long idComunicado) {
		Optional<Comunicado> comunicadoOpt = comunicadoRepository.findById( idComunicado );
		
		if( !comunicadoOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		
		return modelMapper.map(comunicadoOpt.get(), ComunicadoDto.class);
	}

	@Override
	public Collection<ComunicadoDto> buscarPorInmuebleId(Long idInmueble) {
		return StreamSupport.stream(comunicadoRepository.findByInmuebleId( idInmueble ).spliterator(), false)
				.map(comunicado -> modelMapper.map(comunicado, ComunicadoDto.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<ComunicadoDto> buscarPorContadorId(Long idContador) {
		return StreamSupport.stream(comunicadoRepository.findByContadorId( idContador ).spliterator(), false)
				.map(comunicado -> modelMapper.map(comunicado, ComunicadoDto.class))
				.collect(Collectors.toList());
	}

}
