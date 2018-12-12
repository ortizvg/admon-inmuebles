package mx.com.admoninmuebles.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.ComentarioDto;
import mx.com.admoninmuebles.persistence.model.Comentario;
import mx.com.admoninmuebles.persistence.repository.ComentarioRepository;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Comentario save(final ComentarioDto comentarioDto) {
        return comentarioRepository.save(modelMapper.map(comentarioDto, Comentario.class));
    }

}
