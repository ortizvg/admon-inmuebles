package mx.com.admoninmuebles.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.admoninmuebles.dto.PrivilegioDto;
import mx.com.admoninmuebles.service.PrivilegioService;

@Controller
public class PrivilegioController {

    @Autowired
    private PrivilegioService privilegioService;

    @GetMapping(value = "/crearPrivilegio")
    public String showForm(final PrivilegioDto privilegioDto) {
        return "crearPrivilegio";
    }

    @PostMapping(value = "/crearPrivilegio")
    public String crearPrivilegio(final Locale locale, final Model model, @Valid final PrivilegioDto privilegioDto, final BindingResult bindingResult) {
        privilegioService.save(privilegioDto);
        return "redirect:/crearPrivilegio";
    }

}
