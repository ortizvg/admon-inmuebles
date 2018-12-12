package mx.com.admoninmuebles.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.admoninmuebles.dto.RolDto;
import mx.com.admoninmuebles.service.RolService;

@Controller
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping(value = "/crearRol")
    public String showForm(final RolDto rolDto) {
        return "crearRol";
    }

    @PostMapping(value = "/crearRol")
    public String crearRol(final Locale locale, final Model model, @Valid final RolDto rolDto, final BindingResult bindingResult) {
        rolService.save(rolDto);
        return "redirect:/crearRol";
    }

}
