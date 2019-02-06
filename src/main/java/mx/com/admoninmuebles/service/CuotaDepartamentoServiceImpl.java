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

import mx.com.admoninmuebles.dto.CuotaDepartamentoDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Archivo;
import mx.com.admoninmuebles.persistence.model.CuotaDepartamento;
import mx.com.admoninmuebles.persistence.repository.ArchivoRepository;
import mx.com.admoninmuebles.persistence.repository.CuotaDepartamentoRepository;

@Service
public class CuotaDepartamentoServiceImpl implements CuotaDepartamentoService{
	
	@Autowired
	private CuotaDepartamentoRepository cuotaDepartamentoRepository;
	
	@Autowired
	private ArchivoRepository archivoRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Transactional
	@Override
	public CuotaDepartamentoDto guardar(CuotaDepartamentoDto cuotaDepartamentoDto) {
		Archivo archivoReporteCreado = null;
		Archivo archivo = new Archivo();
		archivo.setId(UUID.randomUUID().toString());
		archivo.setBytes(cuotaDepartamentoDto.getArchivoBytes());
		archivo.setNombre(cuotaDepartamentoDto.getArchivoNombre());
		archivo.setTipoContenido(cuotaDepartamentoDto.getArchivoTipoContenido());
		archivoReporteCreado = archivoRepository.save(archivo);
		
		CuotaDepartamento cuotaDepartamento = modelMapper.map(cuotaDepartamentoDto, CuotaDepartamento.class);
		cuotaDepartamento.setArchivo(archivoReporteCreado);
		
		cuotaDepartamento = cuotaDepartamentoRepository.save( cuotaDepartamento );
		
		return modelMapper.map(cuotaDepartamento, CuotaDepartamentoDto.class);
	}

	@Override
	public CuotaDepartamentoDto actualizar(CuotaDepartamentoDto cuotaDepartamentoDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public void eliminar(Long idCuotaDepartamento) {
		Optional<CuotaDepartamento> cuotaDepartamentoOpt = cuotaDepartamentoRepository.findById( idCuotaDepartamento );
		
		if( !cuotaDepartamentoOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		
		archivoRepository.deleteById(cuotaDepartamentoOpt.get().getArchivo().getId());
		cuotaDepartamentoRepository.deleteById(cuotaDepartamentoOpt.get().getId());
		
		
	}

	@Override
	public CuotaDepartamentoDto buscarPorId(Long idCuotaDepartamento) {
		Optional<CuotaDepartamento> cuotaDepartamentoOpt = cuotaDepartamentoRepository.findById( idCuotaDepartamento );
		
		if( !cuotaDepartamentoOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		
		return modelMapper.map(cuotaDepartamentoOpt.get(), CuotaDepartamentoDto.class);
	}

	@Override
	public Collection<CuotaDepartamentoDto> buscarPorInmuebleId(Long idInmueble) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<CuotaDepartamentoDto> buscarPorSocioId(Long idsocio) {
		return StreamSupport.stream(cuotaDepartamentoRepository.findBySocioId( idsocio ).spliterator(), false)
				.map(cuotaDepartamento -> modelMapper.map(cuotaDepartamento, CuotaDepartamentoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public CuotaDepartamentoDto buscarRecientePorSocioId(Long idsocio) {
		Optional<CuotaDepartamento> cuotaDepartamentoOpt = cuotaDepartamentoRepository.findFirst1BySocioIdOrderByFechaModificacionDesc( idsocio );
		
		if( !cuotaDepartamentoOpt.isPresent() ) {
			throw new BusinessException("consulta.noresultados");
		}
		
		return modelMapper.map(cuotaDepartamentoOpt.get(), CuotaDepartamentoDto.class);
	}
	
}
