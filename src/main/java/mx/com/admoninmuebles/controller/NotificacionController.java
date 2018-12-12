package mx.com.admoninmuebles.controller;

import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.NotificacionDto;
import mx.com.admoninmuebles.dto.ZonaDto;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.NotificacionService;
import mx.com.admoninmuebles.service.ZonaService;

@Controller
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;
    
	@Autowired
	private InmuebleService inmuebleService;
	
	@Autowired
	private ZonaService zonaService;
	
    @GetMapping(value = "/notificaciones")
    public String showNotificaciones(final Model model) {
    	model.addAttribute("notificacionSeleccionadaId", null);
    	return "notificaciones/notificaciones";
    }
    
    @GetMapping(value = "/notificaciones/{id}")
    public String showNotificacion(final Model model, final @PathVariable long id) {
    	model.addAttribute("notificacionSeleccionadaId", id);
    	return "notificaciones/notificaciones";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/notificaciones")
    public String init(Model model, final HttpServletRequest request) {
    	
    	 if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
    		 model.addAttribute("notificaciones", notificacionService.findAll());
             
         } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
        	 Long adminZonaLogueadoId = SecurityUtils.getCurrentUserId().get();
         	 ZonaDto zona = zonaService.findByAdminZonaId(adminZonaLogueadoId).stream().findFirst().get();
         	 model.addAttribute("notificaciones", notificacionService.findByZonaId( zona.getCodigo() ));
         
         } else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
        	 Long adminBiId = SecurityUtils.getCurrentUserId().get();
        	 model.addAttribute("notificaciones", notificacionService.findByInmuebleAdminBiId( adminBiId ));
         }
    	return "catalogos/notificaciones";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_BI')")
    @GetMapping(value = "/catalogos/notificacion-crear")
    public String guardarNotificacion(final NotificacionDto notificacionDto, Model model, final HttpSession session) {
    	Optional<Long> optId = SecurityUtils.getCurrentUserId();
        if (optId.isPresent()) {
            session.setAttribute("inmueblesDto", inmuebleService.findByAdminBiId(optId.get()));
        }
        return "catalogos/notificacion-crear";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_BI')")
    @PostMapping(value = "/catalogos/notificacion-crear")
    public String guardarNotificacion(final Locale locale, final Model model, @Valid final NotificacionDto notificacionDto, final BindingResult bindingResult) {
    	 if (bindingResult.hasErrors()) {
             return "catalogos/notificacion-crear";
         }
    	notificacionService.save(notificacionDto);
        return "redirect:/catalogos/notificaciones";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_BI')")
    @GetMapping(value = "/catalogos/notificacion-editar/{idNotificacion}")
    public String buscarNotificacionPorId(final @PathVariable long idNotificacion, Model model, final HttpSession session) {
    	model.addAttribute("notificacionDto", notificacionService.findById(idNotificacion));
    	Optional<Long> optId = SecurityUtils.getCurrentUserId();
        if (optId.isPresent()) {
            session.setAttribute("inmueblesDto", inmuebleService.findByAdminBiId(optId.get()));
        }
        return "catalogos/notificacion-edicion";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_BI')")
    @PostMapping(value = "/catalogos/notificacion-editar")
    public String editarNotificacion(final Locale locale, final Model model, @Valid final NotificacionDto notificacionDto, final BindingResult bindingResult) {
    	notificacionService.save(notificacionDto);
        return "redirect:/catalogos/notificaciones";
    }
    
    @GetMapping(value = "/catalogos/notificacion-eliminar/{idNotificacion}")
    public String eliminarNotificacion(final @PathVariable long idNotificacion) {
    	notificacionService.deleteById(idNotificacion);;
        return "redirect:/catalogos/notificaciones";
    }

}
