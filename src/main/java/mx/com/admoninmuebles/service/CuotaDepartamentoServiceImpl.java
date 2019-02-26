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
		Archivo archivoCreado = null;
    	if(  cuotaDepartamentoDto.getArchivoMP() != null ) {
    		try {
    			validarArhivoCuotDepartamento( cuotaDepartamentoDto );
    			Archivo archivo = new Archivo();
    			archivo.setId(UUID.randomUUID().toString());
    			archivo.setBytes(cuotaDepartamentoDto.getArchivoMP().getBytes());
    			archivo.setNombre(cuotaDepartamentoDto.getArchivoMP().getOriginalFilename());
    			archivo.setTipoContenido(cuotaDepartamentoDto.getArchivoMP().getContentType());
    			archivoCreado = archivoRepository.save(archivo);
    			
    		} catch (IOException e) {
    			throw new BusinessException("cuota.departamento.archivo.guardado.error");
    		}
    	}
		
		CuotaDepartamento cuotaDepartamento = modelMapper.map(cuotaDepartamentoDto, CuotaDepartamento.class);
		cuotaDepartamento.setArchivo(archivoCreado);
		
		cuotaDepartamento = cuotaDepartamentoRepository.save( cuotaDepartamento );
		
		return modelMapper.map(cuotaDepartamento, CuotaDepartamentoDto.class);
	}
	
	private void validarArhivoCuotDepartamento(CuotaDepartamentoDto cuotaDepartamentoDto) {
		
		if( !MediaType.APPLICATION_PDF_VALUE.equalsIgnoreCase( cuotaDepartamentoDto.getArchivoMP().getContentType() ) ) {
			throw new BusinessException("cuota.departamento.archivo.validacion.mediatype.pdf");
		}
		
		if( cuotaDepartamentoDto.getArchivoMP().getSize() > ComunConst.TAMANIO_2_MB ) {
			throw new BusinessException("cuota.departamento.archivo.validacion.tamanio");
		}
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
		return StreamSupport.stream(cuotaDepartamentoRepository.findByInmuebleId( idInmueble ).spliterator(), false)
				.map(cuotaDepartamento -> modelMapper.map(cuotaDepartamento, CuotaDepartamentoDto.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<CuotaDepartamentoDto> buscarPorContadorId(Long idContador) {
		return StreamSupport.stream(cuotaDepartamentoRepository.findByContadorId( idContador ).spliterator(), false)
				.map(cuotaDepartamento -> modelMapper.map(cuotaDepartamento, CuotaDepartamentoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<CuotaDepartamentoDto> buscarPorAdminBiId(Long idAdminBi) {
		return StreamSupport.stream(cuotaDepartamentoRepository.findByAdminBiId( idAdminBi ).spliterator(), false)
				.map(cuotaDepartamento -> modelMapper.map(cuotaDepartamento, CuotaDepartamentoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<CuotaDepartamentoDto> buscarPorAdminZonaId(Long idAdminZona) {
		return StreamSupport.stream(cuotaDepartamentoRepository.findByAdminZonaId( idAdminZona ).spliterator(), false)
				.map(cuotaDepartamento -> modelMapper.map(cuotaDepartamento, CuotaDepartamentoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<CuotaDepartamentoDto> buscarTodo() {
		return StreamSupport.stream(cuotaDepartamentoRepository.findAll().spliterator(), false)
				.map(cuotaDepartamento -> modelMapper.map(cuotaDepartamento, CuotaDepartamentoDto.class))
				.collect(Collectors.toList());
	}

}
