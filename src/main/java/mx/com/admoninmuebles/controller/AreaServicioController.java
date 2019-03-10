package mx.com.admoninmuebles.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.com.admoninmuebles.dto.AreaServicioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.service.AreaServicioService;

@Controller
public class AreaServicioController {

    @Autowired
    private AreaServicioService areaServicioService;
    
    @Autowired
    private MessageSource messages;

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/areas-servicio")
    public String init(final AreaServicioDto areaServicioDto, final Model model) {
        model.addAttribute("areasServicio", areaServicioService.findAll());
        return "/catalogos/areas-servicio";
    }

//    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PreAuthorize("hasAnyRole('ADMIN_CORP')")
    @GetMapping(value = "/catalogos/area-servicio-crear")
    public String showForm(final AreaServicioDto areaServicioDto) {
        return "/catalogos/area-servicio-crear";
    }

//    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PreAuthorize("hasAnyRole('ADMIN_CORP')")
    @PostMapping(value = "/areaServicio")
    public String crearAreaServicio(final Locale locale, final Model model, @Valid final AreaServicioDto areaServicioDto, final BindingResult bindingResult, RedirectAttributes redirect) {
    	try {
    		areaServicioService.save(areaServicioDto);
    	} catch (BusinessException e) {
    		redirect.addFlashAttribute("error", messages.getMessage(e.getMessage(), null, locale));
			return "redirect:/catalogos/area-servicio-crear"; 
    	}
        return "redirect:/catalogos/areas-servicio";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/area-servicio-editar/{id}")
    public String findById(final @PathVariable long id, final Model model) {
        model.addAttribute("areaServicioDto", areaServicioService.findById(id));
        return "/catalogos/area-servicio-crear";
    }

//    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PreAuthorize("hasAnyRole('ADMIN_CORP')")
    @GetMapping(value = "/catalogos/area-servicio-eliminar/{id}")
    public String eliminarAreaServicio(final @PathVariable long id) {
        areaServicioService.delete(id);
        return "redirect:/catalogos/areas-servicio";
    }

}
