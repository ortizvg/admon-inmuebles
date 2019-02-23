package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.NotificacionDto;
import mx.com.admoninmuebles.persistence.model.Notificacion;

public interface NotificacionService {
    Notificacion save(NotificacionDto notificacionDto);
    Collection<NotificacionDto> findAll();
    NotificacionDto findById(Long idNotificacion);
    Collection<NotificacionDto> findByInmuebleId(Long id);
    Collection<NotificacionDto> findByInmuebleIdNotExpired(Long id);
    void deleteById(Long idNotificacion);
    Collection<NotificacionDto> findByZonaId(String id);
    Collection<NotificacionDto> findByInmuebleAdminBiId(Long id);
    Collection<NotificacionDto> findByUserIdNotExpired(Long idUsuario);
    Collection<NotificacionDto> findBySocioIdtExpired(Long idSocio);
    
    Collection<NotificacionDto> findByAdminBiId(Long idAdminBi);
    Collection<NotificacionDto> findByAdminZonaId(Long idAdminZona);
    
    void notificarUsuario(final NotificacionDto notificacionDto);
    void notificarInmueble(final NotificacionDto notificacionDto);
}
