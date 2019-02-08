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

import mx.com.admoninmuebles.dto.EstadoCuentaDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Archivo;
import mx.com.admoninmuebles.persistence.model.EstadoCuenta;
import mx.com.admoninmuebles.persistence.repository.ArchivoRepository;
import mx.com.admoninmuebles.persistence.repository.EstadoCuentaRepository;

@Service
public class EstadoCuentaServiceImpl implements EstadoCuentaService{
	
	@Autowired
	private EstadoCuentaRepository estadoCuentaRepository;
	
	@Autowired
	private ArchivoRepository archivoRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Transactional
	@Override
	public EstadoCuentaDto guardar(EstadoCuentaDto estadoCuentaDto) {
		Archivo archivoCreado = null;
    	if(  estadoCuentaDto.getArchivoMP() != null ) {
    		try {
    			
    			Archivo archivo = new Archivo();
    			archivo.setId(UUID.randomUUID().toString());
    			archivo.setBytes(estadoCuentaDto.getArchivoMP().getBytes());
    			archivo.setNombre(estadoCuentaDto.getArchivoMP().getOriginalFilename());
    			archivo.setTipoContenido(estadoCuentaDto.getArchivoMP().getContentType());
    			archivoCreado = archivoRepository.save(archivo);
    			
    		} catch (IOException e) {
    			throw new BusinessException("pago.error.carga.archivo");
    		}
    	}
		
		EstadoCuenta estadoCuenta = modelMapper.map(estadoCuentaDto, EstadoCuenta.class);
		estadoCuenta.setArchivo(archivoCreado);
		
		estadoCuenta = estadoCuentaRepository.save(estadoCuenta);
		
		return modelMapper.map(estadoCuenta, EstadoCuentaDto.class);
	}

	@Override
	public EstadoCuentaDto actualizar(EstadoCuentaDto estadoCuentaDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public void eliminar(Long idEstadoCuenta) {
		Optional<EstadoCuenta> estadoCuentaOpt = estadoCuentaRepository.findById( idEstadoCuenta );
		
		if( !estadoCuentaOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		
		estadoCuentaRepository.deleteById(estadoCuentaOpt.get().getId());
		
		
	}

	@Override
	public EstadoCuentaDto buscarPorId(Long idEstadoCuenta) {
		
		Optional<EstadoCuenta> estadoCuentaOpt = estadoCuentaRepository.findById( idEstadoCuenta );
		
		if( !estadoCuentaOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		
		return modelMapper.map(estadoCuentaOpt.get(), EstadoCuentaDto.class);
	}

	@Override
	public Collection<EstadoCuentaDto> buscarPorInmuebleId(Long idInmueble) {
		return StreamSupport.stream(estadoCuentaRepository.findByInmuebleId( idInmueble ).spliterator(), false)
				.map(estadoCuenta -> modelMapper.map(estadoCuenta, EstadoCuentaDto.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<EstadoCuentaDto> buscarPorContadorId(Long idContador) {
		return StreamSupport.stream(estadoCuentaRepository.findByContadorId( idContador ).spliterator(), false)
				.map(estadoCuenta -> modelMapper.map(estadoCuenta, EstadoCuentaDto.class))
				.collect(Collectors.toList());
	}
	
	
	@Override
	public Collection<EstadoCuentaDto> buscarPorSocioId(Long idsocio) {
		return StreamSupport.stream(estadoCuentaRepository.findBySocioId( idsocio ).spliterator(), false)
				.map(estadoCuenta -> modelMapper.map(estadoCuenta, EstadoCuentaDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public EstadoCuentaDto buscarRecientePorSocioId(Long idsocio) {
		Optional<EstadoCuenta> estadoCuentaOpt = estadoCuentaRepository.findFirst1BySocioIdOrderByFechaModificacionDesc( idsocio );
		
		if( !estadoCuentaOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		
		return modelMapper.map(estadoCuentaOpt.get(), EstadoCuentaDto.class);
	}
	

}
