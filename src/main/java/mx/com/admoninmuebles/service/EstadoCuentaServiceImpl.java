package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.constant.ComunConst;
import mx.com.admoninmuebles.dto.EstadoCuentaDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Archivo;
import mx.com.admoninmuebles.persistence.model.EstadoCuenta;
import mx.com.admoninmuebles.persistence.repository.ArchivoRepository;
import mx.com.admoninmuebles.persistence.repository.EstadoCuentaRepository;

@Service
@Transactional
public class EstadoCuentaServiceImpl implements EstadoCuentaService{
	
	@Autowired
	private EstadoCuentaRepository estadoCuentaRepository;
	
	@Autowired
	private ArchivoRepository archivoRepository;
	
	@Autowired
	private InmuebleService inmuebleService;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public EstadoCuentaDto guardar(EstadoCuentaDto estadoCuentaDto) {
		Archivo archivoCreado = null;
    	if(  estadoCuentaDto.getArchivoMP() != null ) {
//    		try {
    			validarArhivoEstadoCuenta( estadoCuentaDto );
    			Archivo archivo = new Archivo();
    			archivo.setId(UUID.randomUUID().toString());
//    			archivo.setBytes(estadoCuentaDto.getArchivoMP().getBytes());
//    			archivo.setNombre(estadoCuentaDto.getArchivoMP().getOriginalFilename());
//    			archivo.setTipoContenido(estadoCuentaDto.getArchivoMP().getContentType());
    			archivo.setBytes(estadoCuentaDto.getArchivoBytes());
    			archivo.setNombre(estadoCuentaDto.getArchivoNombre());
    			archivo.setTipoContenido(estadoCuentaDto.getArchivoTipoContenido());
    			archivoCreado = archivoRepository.save(archivo);
    			
//    		} catch (IOException e) {
//    			e.printStackTrace();
//    			throw new BusinessException("estado.cuenta.archivo.guardado.error");
//    		}
    	}
		
		EstadoCuenta estadoCuenta = modelMapper.map(estadoCuentaDto, EstadoCuenta.class);
		estadoCuenta.setArchivo(archivoCreado);
		
		estadoCuenta = estadoCuentaRepository.save(estadoCuenta);
		
		return modelMapper.map(estadoCuenta, EstadoCuentaDto.class);
	}
	
	@Async
	@Override
	public void guardarPorInmueble(EstadoCuentaDto estadoCuentaDto) {
		
		Collection<UsuarioDto> socios = inmuebleService.findSociosActivosByInmuebleId( estadoCuentaDto.getInmuebleId() );
		socios.stream().forEach(socio -> {
			estadoCuentaDto.setSocioId(socio.getId());
			guardar( estadoCuentaDto );
		});
	}
	
	private void validarArhivoEstadoCuenta(EstadoCuentaDto estadoCuentaDto) {
		
		if( !MediaType.APPLICATION_PDF_VALUE.equalsIgnoreCase( estadoCuentaDto.getArchivoMP().getContentType() ) ) {
			throw new BusinessException("cuota.departamento.archivo.validacion.mediatype.pdf");
		}
		
		if( estadoCuentaDto.getArchivoMP().getSize() > ComunConst.TAMANIO_1_MB ) {
			throw new BusinessException("cuota.departamento.archivo.validacion.tamanio");
		}
	}


	@Override
	public EstadoCuentaDto actualizar(EstadoCuentaDto estadoCuentaDto) {
		// TODO Auto-generated method stub
		return null;
	}

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

	@Override
	public Collection<EstadoCuentaDto> buscarPorAdminBiId(Long idAdminBi) {
		return StreamSupport.stream(estadoCuentaRepository.findByAdminBiId( idAdminBi ).spliterator(), false)
				.map(estadoCuenta -> modelMapper.map(estadoCuenta, EstadoCuentaDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<EstadoCuentaDto> buscarPorAdminZonaId(Long idAdminZona) {
		return StreamSupport.stream(estadoCuentaRepository.findByAdminZonaId( idAdminZona ).spliterator(), false)
				.map(estadoCuenta -> modelMapper.map(estadoCuenta, EstadoCuentaDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<EstadoCuentaDto> buscarTodo() {
		return StreamSupport.stream(estadoCuentaRepository.findAll().spliterator(), false)
				.map(estadoCuenta -> modelMapper.map(estadoCuenta, EstadoCuentaDto.class))
				.collect(Collectors.toList());
	}
	

}
