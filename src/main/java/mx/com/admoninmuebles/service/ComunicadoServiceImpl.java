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
	private ModelMapper modelMapper;

	@Transactional
	@Override
	public ComunicadoDto guardar(ComunicadoDto comunicadoDto) {
		Archivo archivoReporteCreado = null;
		Archivo archivo = new Archivo();
		archivo.setId(UUID.randomUUID().toString());
		archivo.setBytes(comunicadoDto.getArchivoBytes());
		archivo.setNombre(comunicadoDto.getArchivoNombre());
		archivo.setTipoContenido(comunicadoDto.getArchivoTipoContenido());
		archivoReporteCreado = archivoRepository.save(archivo);
		
		Comunicado comunicado = modelMapper.map(comunicadoDto, Comunicado.class);
		comunicado.setArchivo(archivoReporteCreado);
		
		comunicado = comunicadoRepository.save(comunicado);
		
		return modelMapper.map(comunicado, ComunicadoDto.class);
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
		
		archivoRepository.deleteById(comunicadoOpt.get().getArchivo().getId());
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

}
