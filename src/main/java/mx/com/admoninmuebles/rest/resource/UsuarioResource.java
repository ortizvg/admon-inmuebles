package mx.com.admoninmuebles.rest.resource;


import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.admoninmuebles.dto.CambioContraseniaDto;
import mx.com.admoninmuebles.dto.ReactivaUsuarioDto;
import mx.com.admoninmuebles.dto.RecuperacionContraseniaCorreoDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.listener.event.OnRecuperacionContraseniaEvent;
import mx.com.admoninmuebles.listener.event.OnRegistroCompletoEvent;
import mx.com.admoninmuebles.service.UsuarioService;

@RestController
@RequestMapping("/api")
public class UsuarioResource {
	
    @Autowired
    private ApplicationEventPublisher eventPublisher;
	
    @Autowired
    private UsuarioService userService;
    
    @Autowired
    private MessageSource messages;
//
//	@GetMapping("/usuarios/{login}/recuperar-contrasenia")
//	public ResponseEntity<String> recuperarContrasenia(final Locale locale, final HttpServletRequest request,  @PathVariable String login) {
//		System.out.println("Iniciando validacion usuario" + login);
//		try {
//			if(login == null || login.trim().isEmpty()) {
//				System.out.println("login nulo o vacio" + login);
//				return new ResponseEntity<String>(Boolean.FALSE.toString(), HttpStatus.BAD_REQUEST);
//			}
//			UsuarioDto usuarioDto = userService.findByUsernameOrCorreo(login);
//			eventPublisher.publishEvent(new OnRecuperacionContraseniaEvent(usuarioDto, request.getLocale(), getAppUrl(request)));
//		} catch(UsernameNotFoundException e) {
//			return new ResponseEntity<String>(Boolean.FALSE.toString(), HttpStatus.OK);
//		}
//		
//		return new ResponseEntity<String>(Boolean.TRUE.toString(), HttpStatus.OK);
//	}
	
	@PostMapping("/usuarios/recuperar-contrasenia")
	public ResponseEntity<String> recuperarContrasenia(final Locale locale, final HttpServletRequest request,  @Valid final RecuperacionContraseniaCorreoDto recuperacionContraseniaCorreoDto, final BindingResult bindingResult) {
		System.out.println("Iniciando validacion usuario" + recuperacionContraseniaCorreoDto.getLogin());
		try {
			if(bindingResult.hasFieldErrors()) {
				return new ResponseEntity<String>(Boolean.FALSE.toString(), HttpStatus.BAD_REQUEST);
			}
			UsuarioDto usuarioDto = userService.findByUsernameOrCorreo(recuperacionContraseniaCorreoDto.getLogin());
			userService.enviarCorreoRecuperacionContrasenia(usuarioDto,  getAppUrl(request));
//			eventPublisher.publishEvent(new OnRecuperacionContraseniaEvent(usuarioDto, request.getLocale(), getAppUrl(request)));
		} catch(UsernameNotFoundException e) {
			return new ResponseEntity<String>(Boolean.FALSE.toString(), HttpStatus.OK);
		}
		
		return new ResponseEntity<String>(Boolean.TRUE.toString(), HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/usuarios/cambioContrasenia")
    public ResponseEntity<String> cambiarContrasenia(final Locale locale, final Model model, @Valid final CambioContraseniaDto cambioContraseniaDto, BindingResult result) {
    	
    	try {
    		userService.cambiarContrasenia(cambioContraseniaDto);
    		return new ResponseEntity<String>(messages.getMessage("usuario.actualiza.contrasenia.ok", null, locale), HttpStatus.OK);
    	} catch(BusinessException e) {
    		return new ResponseEntity<String>(messages.getMessage(e.getMessage(), null, locale), HttpStatus.BAD_REQUEST);
    	}
    	
    }
    
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/usuarios/reactivacion")
	public ResponseEntity<String> reactivarUsuario(final Locale locale, final HttpServletRequest request, @Valid final ReactivaUsuarioDto reactivaUsuarioDto) {
		System.out.println("Iniciando reactivacion de usuario " + reactivaUsuarioDto.getUsername());
		try {
			UsuarioDto usuarioDto = userService.findByUsernameOrCorreo(reactivaUsuarioDto.getUsername());
			userService.enviarCorreoActivacion(usuarioDto, getAppUrl(request));
//			eventPublisher.publishEvent(new OnRegistroCompletoEvent(usuarioDto, request.getLocale(), getAppUrl(request)));
		} catch(UsernameNotFoundException e) {
			return new ResponseEntity<String>(messages.getMessage(e.getMessage(), null, locale), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<String>(messages.getMessage("usuario.reactivacion.ok", null, locale), HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/findAdministradoresBiByZonaCodigo")
	public ResponseEntity<Collection<UsuarioDto>> buscarColonias( @RequestParam(name = "zonaCodigo", required = false) String zonaCodigo) {
		try {
			Collection<UsuarioDto> usuarios = null;
			if(zonaCodigo != null && !zonaCodigo.isEmpty()){
				usuarios = userService.findAdministradoresBiByZonaCodigo(zonaCodigo);
			}else {
				usuarios = Collections.emptyList();
			}
			return new ResponseEntity<>(usuarios, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		}
		
	}
    
    
    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
