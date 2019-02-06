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
		Archivo archivoReporteCreado = null;
		Archivo archivo = new Archivo();
		archivo.setId(UUID.randomUUID().toString());
		archivo.setBytes(estadoCuentaDto.getArchivoBytes());
		archivo.setNombre(estadoCuentaDto.getArchivoNombre());
		archivo.setTipoContenido(estadoCuentaDto.getArchivoTipoContenido());
		archivoReporteCreado = archivoRepository.save(archivo);
		
		EstadoCuenta estadoCuenta = modelMapper.map(estadoCuentaDto, EstadoCuenta.class);
		estadoCuenta.setArchivo(archivoReporteCreado);
		
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
		
		archivoRepository.deleteById(estadoCuentaOpt.get().getArchivo().getId());
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
		// TODO Auto-generated method stub
		return null;
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
