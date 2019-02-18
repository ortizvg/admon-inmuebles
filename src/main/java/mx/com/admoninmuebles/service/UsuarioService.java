package mx.com.admoninmuebles.service;

import java.util.Collection;

import mx.com.admoninmuebles.dto.CambioContraseniaDto;
import mx.com.admoninmuebles.dto.UsuarioDto;

public interface UsuarioService {

    UsuarioDto editarPerfil(UsuarioDto userDto);

    UsuarioDto editarCuenta(final UsuarioDto userDto);

    UsuarioDto findById(Long idUsuario);

    UsuarioDto findByUsernameOrCorreo(final String usernameCorreo);

    void deleteById(Long idUsuario);

    Collection<UsuarioDto> findAll();

    UsuarioDto crearCuenta(UsuarioDto userDto);

    UsuarioDto findUserLogin();

    UsuarioDto cambiarContrasenia(CambioContraseniaDto cambioContraseniaDto);

    Collection<UsuarioDto> findByRolesNombre(String nombre);

    Collection<UsuarioDto> findByRolesNombreAndAreasServicioId(String nombre, Long id);

    Collection<UsuarioDto> findAllAdministradores();

    Collection<UsuarioDto> findAdministradoresBiByZonaCodigo(String zonaCodigo);
    
    void enviarCorreoActivacion(UsuarioDto usuario, final String urlContext);
    
    void enviarCorreoRecuperacionContrasenia(UsuarioDto usuario, final String urlContext);
}
