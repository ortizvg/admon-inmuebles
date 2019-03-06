package mx.com.admoninmuebles.controller;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.dto.RolDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.ContadorService;
import mx.com.admoninmuebles.service.NotificacionService;
import mx.com.admoninmuebles.service.PagoService;
import mx.com.admoninmuebles.service.RolService;
import mx.com.admoninmuebles.service.UsuarioService;

@Controller
public class ContadorController {
	
	
	Logger logger = LoggerFactory.getLogger(ContadorController.class);
	
    @Autowired
    private MessageSource messages;
	
	@Autowired
	private NotificacionService notificacionService;
	
	@Autowired
	private PagoService pagoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ContadorService contadorService;
	
    @Autowired
    private RolService rolService;
	
	@GetMapping("/contador")
	public String mostrarInicio(Model model,  final HttpSession session) {
		Long idContador = SecurityUtils.getCurrentUserId().get();
		Collection<PagoDto> pagos = pagoService.buscarPorContador( idContador );
		model.addAttribute("pagos", pagos);
	    session.setAttribute("notificaciones", notificacionService.findByUserIdNotExpired( idContador ));
		
		return "contador/inicio";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
	@GetMapping("/contadores")
	public String listarContadores(Model model,  final HttpSession session) {
		model.addAttribute("usuarios", contadorService.buscarTodos());
		
		return "contadores/contadores";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
	@GetMapping("/contadores/nuevo")
	public String crearContadorForm(Model model,  final HttpSession session) {
		model.addAttribute("usuarioDto", new UsuarioDto());
		return "contadores/contador-crear";
	}
	
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @PostMapping(value = "/contadores/nuevo")
    public String crearContador(final HttpServletRequest request, final Locale locale, final Model model, @Valid final UsuarioDto usuarioDto, final BindingResult bindingResult) {
    	logger.info(usuarioDto.toString());
        if (bindingResult.hasErrors()) {
            return "contadores/contador-crear";
        }
        
        try {
        	RolDto rolSocio = rolService.findByNombre(RolConst.ROLE_CONTADOR);
        	usuarioDto.setRolSeleccionado(rolSocio.getId());
	        UsuarioDto contadorNuevo = (UsuarioDto) usuarioService.crearCuenta(usuarioDto);
	        usuarioService.enviarCorreoActivacion( contadorNuevo , getAppUrl( request ) );
	        return "redirect:/contadores";
        }catch(BusinessException e) {
   		 bindingResult.addError(new ObjectError(messages.getMessage(e.getMessage(), null, locale), messages.getMessage(e.getMessage(), null, locale)));
   		 return "contadores/contador-crear";
   	 	}
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @GetMapping(value = "/contadores/{id}/edicion")
    public String edicionInit(final @PathVariable Long id, final Model model, final HttpSession session, final HttpServletRequest request) {
    	UsuarioDto usuarioDto = null;
    	try {
    		usuarioDto = usuarioService.findById(id);
    	} catch (BusinessException e) {
    		return "error/404";
		}
    	model.addAttribute("usuarioDto", usuarioDto);
        return "contadores/contador-editar";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @PostMapping(value = "/contadores/edicion")
    public String editar(final Locale locale, final Model model, @Valid final UsuarioDto usuarioDto, final BindingResult bindingResult) {
    	if (bindingResult.hasErrors()) {
            return "contadores/contador-editar";
        }
    	RolDto rolSocio = rolService.findByNombre(RolConst.ROLE_CONTADOR);
    	usuarioDto.setRolSeleccionado(rolSocio.getId());
    	usuarioService.editarCuenta(usuarioDto);
    	return "redirect:/contadores";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @GetMapping(value = "/contadores/{id}/detalle")
    public String verContador(final @PathVariable Long id, final Model model, final HttpSession session, final HttpServletRequest request) {
    	UsuarioDto usuarioDto = null;
    	try {
    		usuarioDto = usuarioService.findById(id);
    	} catch (BusinessException e) {
    		return "error/404";
		}
    	model.addAttribute("usuarioDto", usuarioDto);
        return "contadores/contador-ver";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @GetMapping(value = "/contadores/{id}/eliminacion")
    public String eliminarSocio(final @PathVariable Long id) {
    	try {
    		usuarioService.deleteById(id);
    	} catch (BusinessException e) {
    		return "error/404";
		} catch (DataIntegrityViolationException e) {
    		return "error/403";
		}
        return "redirect:/contadores";
    }
    
    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
