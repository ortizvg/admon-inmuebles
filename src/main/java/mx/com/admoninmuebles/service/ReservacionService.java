package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.ReservacionDto;
import mx.com.admoninmuebles.persistence.model.Reservacion;

public interface ReservacionService {
	
	void validateReservation(ReservacionDto reservacionDto);
	
    Reservacion save(ReservacionDto reservacionDto);

    Collection<ReservacionDto> findAll();

    Collection<ReservacionDto> findByAreaComun(Long areaComunId);

    Collection<ReservacionDto> findByAreaComunAndInmueble(Long areaComunId, Long inmuebleId);

    Collection<ReservacionDto> findBySocio(Long socioId);

    ReservacionDto findById(Long id);

    void delete(Long id);
    
    void deleteNoPaidReservations() ;
}
