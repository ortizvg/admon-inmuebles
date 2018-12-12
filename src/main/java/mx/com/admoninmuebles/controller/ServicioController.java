package mx.com.admoninmuebles.controller;

import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.admoninmuebles.dto.ServicioDto;
import mx.com.admoninmuebles.service.ServicioService;
import mx.com.admoninmuebles.storage.StorageService;

@Controller
public class ServicioController {

    @Autowired
    private StorageService storageService;
    @Autowired
    private ServicioService servicioService;

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/servicios")
    public String init(final ServicioDto servicioDto, final Model model) {
        model.addAttribute("servicios", servicioService.findAll());
        return "catalogos/servicios";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/servicio-crear")
    public String crearServicio(final ServicioDto servicioDto, final HttpSession session, final Model model) {
        return "catalogos/servicio-crear";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PostMapping(value = "/catalogos/servicio-crear")
    public String guardarServicio(final Locale locale, final Model model, @Valid final ServicioDto servicioDto, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/catalogos/servicio-crear";
        }
        servicioDto.setImagenUrl("/" + storageService.store(servicioDto.getImagen()));
        servicioService.save(servicioDto);

        return "redirect:/catalogos/servicios";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/servicio-editar/{id}")
    public String buscarServicioPorId(final @PathVariable Long id, final Model model, final HttpSession session) {
        model.addAttribute("servicioDto", servicioService.findById(id));
        return "catalogos/servicio-editar";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PostMapping(value = "/catalogos/servicio-editar")
    public String editarServicio(final Locale locale, final Model model, @Valid final ServicioDto servicioDto, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/catalogos/servicio-editar";
        }

        if (StringUtils.isEmpty(servicioDto.getImagenUrl())) {
            servicioDto.setImagenUrl("/" + storageService.store(servicioDto.getImagen()));
        }
        servicioService.save(servicioDto);
        return "redirect:/catalogos/servicios";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/servicio-eliminar/{id}")
    public String eliminarServicio(final @PathVariable Long id) {
        servicioService.deleteById(id);
        return "redirect:/catalogos/servicios";
    }

}
