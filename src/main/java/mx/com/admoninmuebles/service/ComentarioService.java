package mx.com.admoninmuebles.service;

import mx.com.admoninmuebles.dto.ComentarioDto;
import mx.com.admoninmuebles.persistence.model.Comentario;

public interface ComentarioService {
    Comentario save(ComentarioDto comentarioDto);
}
