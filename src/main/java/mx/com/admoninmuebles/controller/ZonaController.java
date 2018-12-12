package mx.com.admoninmuebles.controller;

import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.ZonaDto;
import mx.com.admoninmuebles.service.UsuarioService;
import mx.com.admoninmuebles.service.ZonaService;

@Controller
public class ZonaController {

    @Autowired
    private MessageSource messages;

    @Autowired
    private ZonaService zonaService;

    @Autowired
    private UsuarioService usuarioService;

    @PreAuthorize("hasRole('" + RolConst.ROLE_ADMIN_CORP + "')")
    @GetMapping(value = "/catalogos/zonas")
    public String init(final ZonaDto zonaDto, final Model model) {
        model.addAttribute("zonas", zonaService.findAll());
        return "catalogos/zonas";
    }

    @PreAuthorize("hasRole('ADMIN_CORP')")
    @GetMapping(value = "/catalogos/zona-crear")
    public String crearZona(final ZonaDto zonaDto, final HttpSession session) {
        session.setAttribute("usuariosAdminZona", usuarioService.findByRolesNombre(RolConst.ROLE_ADMIN_ZONA));
        return "catalogos/zona-crear";
    }

    @PreAuthorize("hasRole('ADMIN_CORP')")
    @PostMapping(value = "/catalogos/zona-crear")
    public String guardarZona(final Locale locale, final Model model, @Valid final ZonaDto zonaDto, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/catalogos/zona-crear";
        }
        if (zonaService.exist(zonaDto.getCodigo())) {
            FieldError error;
            error = new FieldError("zonaDto", "codigo", zonaDto.getCodigo(), false, null, null, messages.getMessage("mensage.zona.codigoexiste", null, locale));
            bindingResult.addError(error);
            return "/catalogos/zona-crear";
        } else {
            zonaService.save(zonaDto);
        }

        return "redirect:/catalogos/zonas";
    }

    @PreAuthorize("hasRole('ADMIN_CORP')")
    @GetMapping(value = "/catalogos/zona-editar/{codigo}")
    public String buscarZonaPorId(final @PathVariable String codigo, final Model model, final HttpSession session) {
        session.setAttribute("usuariosAdminZona", usuarioService.findByRolesNombre(RolConst.ROLE_ADMIN_ZONA));
        model.addAttribute("zonaDto", zonaService.findById(codigo));
        return "catalogos/zona-editar";
    }

    @PreAuthorize("hasRole('ADMIN_CORP')")
    @PostMapping(value = "/catalogos/zona-editar")
    public String editarZona(final Locale locale, final Model model, @Valid final ZonaDto zonaDto, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/catalogos/zona-editar";
        }
        zonaService.save(zonaDto);
        return "redirect:/catalogos/zonas";
    }

    @PreAuthorize("hasRole('ADMIN_CORP')")
    @GetMapping(value = "/catalogos/zona-eliminar/{codigo}")
    public String eliminarZona(final @PathVariable String codigo) {
        zonaService.deleteById(codigo);
        return "redirect:/catalogos/zonas";
    }

}
