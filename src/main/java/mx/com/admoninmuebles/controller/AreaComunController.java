package mx.com.admoninmuebles.controller;

import java.util.Locale;

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
import mx.com.admoninmuebles.dto.AreaComunDto;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.AreaComunService;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.ZonaService;

@Controller
public class AreaComunController {
    @Autowired
    private AreaComunService areaComunService;

    @Autowired
    private InmuebleService inmuebleService;
    
    @Autowired
    private ZonaService zonaService;

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/areas-comunes")
    public String init(AreaComunDto areaComunDto, final Model model, final HttpServletRequest request, final HttpSession session) {
      
	     Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
	     if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
	    	 model.addAttribute("areasComunes", areaComunService.findByAdminBiId( usuarioLogueadoId ) );
	    	 session.setAttribute("inmuebles", inmuebleService.findByAdminBiId( usuarioLogueadoId ) );
	     } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
	    	 model.addAttribute("areasComunes", areaComunService.findByAdminZonaId( usuarioLogueadoId ) );
	    	 session.setAttribute("zonas", zonaService.findByAdminZonaId( usuarioLogueadoId ) );
	     } else if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
	    	 model.addAttribute("areasComunes", areaComunService.findAll());
	    	 session.setAttribute("zonas", zonaService.findAll() );
	    	 
	     } 
	     
	     if( areaComunService.isFiltro( areaComunDto ) ) {
			model.addAttribute("areasComunes", areaComunService.filtrar( areaComunDto ) );
	     }
	     
	     return "/catalogos/areas-comunes";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/area-comun-crear")
    public String showForm(final AreaComunDto areaComunDto, final HttpSession session, final HttpServletRequest request) {
        Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
	     if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
	    	 session.setAttribute("inmuebles", inmuebleService.findByAdminBiId( usuarioLogueadoId ) );
	     } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
	    	 session.setAttribute("inmuebles", inmuebleService.findByAdminZonaId( usuarioLogueadoId ) );
	     } else if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
	    	 session.setAttribute("inmuebles", inmuebleService.findAll());
	     } 
        return "/catalogos/area-comun-crear";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PostMapping(value = "/catalogos/area-comun-crear")
    public String crearAreaComun(final Locale locale, final Model model, @Valid final AreaComunDto areaComunDto, final BindingResult bindingResult) {
        areaComunService.save(areaComunDto);
        return "redirect:/catalogos/areas-comunes";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/area-comun-editar/{id}")
    public String findById(final @PathVariable long id, final Model model, final HttpSession session, final HttpServletRequest request) {
    	Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
	     if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
	    	 session.setAttribute("inmuebles", inmuebleService.findByAdminBiId( usuarioLogueadoId ) );
	     } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
	    	 session.setAttribute("inmuebles", inmuebleService.findByAdminZonaId( usuarioLogueadoId ) );
	     } else if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
	    	 session.setAttribute("inmuebles", inmuebleService.findAll());
	     } 
        model.addAttribute("areaComunDto", areaComunService.findById(id));
        return "/catalogos/area-comun-crear";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/area-comun-eliminar/{id}")
    public String eliminarAreaComun(final @PathVariable long id) {
        areaComunService.delete(id);
        return "redirect:/catalogos/areas-comunes";
    }

}
