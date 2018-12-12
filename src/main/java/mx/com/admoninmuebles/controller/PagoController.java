package mx.com.admoninmuebles.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.service.PagoService;

@Controller
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping(value = "/crearPago")
    public String showForm(final PagoDto pagoDto) {
        return "crearPago";
    }

    @PostMapping(value = "/crearPago")
    public String crearPago(final Locale locale, final Model model, @Valid final PagoDto pagoDto, final BindingResult bindingResult) {
        pagoService.save(pagoDto);
        return "redirect:/crearPago";
    }

}
