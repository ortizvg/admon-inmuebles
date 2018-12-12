package mx.com.admoninmuebles.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.admoninmuebles.dto.TipoTelefonoDto;
import mx.com.admoninmuebles.service.TipoTelefonoService;

@Controller
public class TipoTelefonoController {

    @Autowired
    private TipoTelefonoService tipoTelefonoService;

    @GetMapping(value = "/crearTipoTelefono")
    public String showForm(final TipoTelefonoDto tipoTelefonoDto) {
        return "crearTipoTelefono";
    }

    @PostMapping(value = "/crearTipoTelefono")
    public String crearTipoTelefono(final Locale locale, final Model model, @Valid final TipoTelefonoDto tipoTelefonoDto, final BindingResult bindingResult) {
        tipoTelefonoService.save(tipoTelefonoDto);
        return "redirect:/crearTipoTelefono";
    }

}
