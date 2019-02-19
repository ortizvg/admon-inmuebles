package mx.com.admoninmuebles.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.ColoniaDto;
import mx.com.admoninmuebles.persistence.model.Asentamiento;
import mx.com.admoninmuebles.persistence.model.Zona;
import mx.com.admoninmuebles.persistence.repository.AsentamientoRepository;
import mx.com.admoninmuebles.persistence.repository.ZonaRepository;

@Service
public class ColoniaServiceImpl implements ColoniaService {

    @Autowired
    private AsentamientoRepository asentamientoRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private ZonaRepository zonaRepository;

    @Override
    public void save(final ColoniaDto coloniaDto) {
        Optional<Asentamiento> optAsentamiento = asentamientoRepository.findById(coloniaDto.getId());
        Asentamiento asentamiento;
        if (optAsentamiento.isPresent()) {
            asentamiento = optAsentamiento.get();
            Zona zona = new Zona();
            zona.setCodigo(coloniaDto.getZonaCodigo());
            asentamiento.setZona(zona);
            asentamientoRepository.save(asentamiento);
        }
    }
    
    @Override
    public void deleteById(final Long codigo) {
        Optional<Asentamiento> optAsentamiento = asentamientoRepository.findById(codigo);
        Asentamiento asentamiento;
        if (optAsentamiento.isPresent()) {
            asentamiento = optAsentamiento.get();
            asentamiento.setZona(null);
            asentamientoRepository.save(asentamiento);
        }

    }

    @Override
    public Collection<ColoniaDto> findAll() {
        return StreamSupport.stream(asentamientoRepository.findAll().spliterator(), false).map(asentamiento -> modelMapper.map(asentamiento, ColoniaDto.class)).collect(Collectors.toList());
    }

    @Override
    public Collection<ColoniaDto> findByZonaIsNotNull() {
        return StreamSupport.stream(asentamientoRepository.findByZonaIsNotNull().spliterator(), false).map(asentamiento -> modelMapper.map(asentamiento, ColoniaDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ColoniaDto> findBycodigoPostal(final String codigoPostal) {
        return StreamSupport.stream(asentamientoRepository.findBycodigoPostal(codigoPostal).spliterator(), false).map(asentamiento -> modelMapper.map(asentamiento, ColoniaDto.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public Collection<ColoniaDto> findBycodigoPostalAndZonaCodigo(final String codigoPostal, String zonaCodigo) {
        return StreamSupport.stream(asentamientoRepository.findBycodigoPostal(codigoPostal).spliterator(), false).map(asentamiento -> modelMapper.map(asentamiento, ColoniaDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ColoniaDto findById(final Long id) {
        Optional<Asentamiento> asentamiento = asentamientoRepository.findById(id);
        return modelMapper.map(asentamiento.get(), ColoniaDto.class);
    }

	@Override
	public Collection<ColoniaDto> findByZonaCodigo(String zonaCodigo) {
		 return StreamSupport.stream(asentamientoRepository.findByZonaCodigo( zonaCodigo ).spliterator(), false).map(asentamiento -> modelMapper.map(asentamiento, ColoniaDto.class))
	                .collect(Collectors.toList());
	}
	
	@Override
	public Collection<ColoniaDto> findByAdminZona(Long adminZonaId) {
		 Collection<Zona> zonas = zonaRepository.findByAdminZonaId( adminZonaId );
		 return StreamSupport.stream(asentamientoRepository.findByZonaIn( zonas ).spliterator(), false).map(asentamiento -> modelMapper.map(asentamiento, ColoniaDto.class))
	                .collect(Collectors.toList());
	}

}
