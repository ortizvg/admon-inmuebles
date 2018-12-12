package mx.com.admoninmuebles.persistence.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.admoninmuebles.persistence.model.Reservacion;

@Repository
public interface ReservacionRepository extends CrudRepository<Reservacion, Long> {
    Collection<Reservacion> findByAreaComunId(Long areaComunId);

    Collection<Reservacion> findByAreaComunIdAndAreaComunInmuebleId(Long areaComunId, Long inmuebleId);

    Collection<Reservacion> findBySocioId(Long socioId);

}
