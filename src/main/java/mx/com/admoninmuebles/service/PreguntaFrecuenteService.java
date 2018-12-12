package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.PreguntaFrecuenteDto;
import mx.com.admoninmuebles.persistence.model.PreguntaFrecuente;

public interface PreguntaFrecuenteService {
    PreguntaFrecuente save(PreguntaFrecuenteDto preguntaFrecuenteDto);
    Collection<PreguntaFrecuenteDto> findAll();
    Collection<PreguntaFrecuenteDto> findByIdioma(String idioma);
    PreguntaFrecuenteDto findById(Long idPreguntaFrecuente);
    void deleteById(Long idPreguntaFrecuente);
}
