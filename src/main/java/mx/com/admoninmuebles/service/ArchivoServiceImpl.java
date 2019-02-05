package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.ArchivoDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Archivo;
import mx.com.admoninmuebles.persistence.repository.ArchivoRepository;

@Service
public class ArchivoServiceImpl implements ArchivoService {
	
	private static final Logger logger = LoggerFactory.getLogger(ArchivoService.class);
	
	@Autowired
	private ArchivoRepository archivoRepository;
	
    @Autowired
    private ModelMapper modelMapper;

	@Override
	public ArchivoDto guardar(ArchivoDto archivoDto) {
		Archivo archivo = modelMapper.map(archivoDto, Archivo.class);
		archivo.setId(UUID.randomUUID().toString());
		return modelMapper.map( archivoRepository.save(archivo), ArchivoDto.class);
	}

	@Override
	public ArchivoDto actualizar(ArchivoDto archivoDto) {
		
		Optional<Archivo> archivoOpt = archivoRepository.findById(archivoDto.getId());
		
		if( !archivoOpt.isPresent() ) {
	           throw new BusinessException("archivo.error.noencontrado");
		}
		
		Archivo archivo = archivoOpt.get();
		
		archivo.setBytes(archivoDto.getBytes());
		archivo.setNombre(archivoDto.getNombre());
		archivo.setTipoContenido(archivoDto.getTipoContenido());
//		archivo.setFechaModificacion(new Date());
//		archivo.setModificadoPor(new Usuario().setId(SecurityUtils.getCurrentUserId().get()));
		
		return modelMapper.map( archivoRepository.save(archivo), ArchivoDto.class);
	}

	@Override
	public void eliminar(ArchivoDto archivoDto) {
		Optional<Archivo> archivoOpt = archivoRepository.findById(archivoDto.getId());
		if( !archivoOpt.isPresent() ) {
	           throw new BusinessException("archivo.error.noencontrado");
		}
		
		archivoRepository.deleteById(archivoDto.getId());
	}

	@Override
	public ArchivoDto buscarPorId(String id) {
		
		logger.info("ID " + id);
		
		Optional<Archivo> archivoOpt = archivoRepository.findById(  id );
		if( !archivoOpt.isPresent() ) {
	           throw new BusinessException("archivo.error.noencontrado");
		}
		
		return modelMapper.map( archivoOpt.get(), ArchivoDto.class);
	}

	@Override
	public Collection<ArchivoDto> buscarTodo() {
		return StreamSupport.stream(archivoRepository.findAll().spliterator(), false)
				.map(archivo -> modelMapper.map(archivo, ArchivoDto.class))
				.collect(Collectors.toList());
	}
	

}
