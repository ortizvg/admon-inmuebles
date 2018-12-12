package mx.com.admoninmuebles.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.admoninmuebles.dto.TipoPagoDto;
import mx.com.admoninmuebles.service.TipoPagoService;

@Controller
public class TipoPagoController {

    @Autowired
    private TipoPagoService tipoPagoService;

    @GetMapping(value = "/crearTipoPago")
    public String showForm(final TipoPagoDto tipoPagoDto) {
        return "crearTipoPago";
    }

    @PostMapping(value = "/crearTipoPago")
    public String crearTipoPago(final Locale locale, final Model model, @Valid final TipoPagoDto tipoPagoDto, final BindingResult bindingResult) {
        tipoPagoService.save(tipoPagoDto);
        return "redirect:/crearTipoPago";
    }
    
    @GetMapping(value = "/catalogos/tipo-pago")
    public String init(final TipoPagoDto tipoPagoDto, Model model) {
    	
    	model.addAttribute("tiposPago", tipoPagoService.findAll());
    	return "catalogos/tipo-pago";
    }
    
    @PostMapping(value = "/catalogos/tipo-pago")
    public String guardarTipoPago(final Locale locale, final Model model, @Valid final TipoPagoDto tipoPagoDto, final BindingResult bindingResult) {
    	tipoPagoService.save(tipoPagoDto);
        return "redirect:/catalogos/tipo-pago";
    }
    
    @GetMapping(value = "/catalogos/tipo-pago-editar/{idTipoPago}")
    public String buscarTipoPagoPorId(final @PathVariable long idTipoPago, Model model) {
    	model.addAttribute("tipoPagoDto", tipoPagoService.findById(idTipoPago));
        return "catalogos/tipo-pago-edicion";
    }
    
    @PostMapping(value = "/catalogos/tipo-pago-editar")
    public String editarTipoPago(final Locale locale, final Model model, @Valid final TipoPagoDto tipoPagoDto, final BindingResult bindingResult) {
    	tipoPagoService.save(tipoPagoDto);
        return "redirect:/catalogos/tipo-pago";
    }
    
    @GetMapping(value = "/catalogos/tipo-pago-eliminar/{idTipoPago}")
    public String eliminarTipoPago(final @PathVariable long idTipoPago) {
    	tipoPagoService.deleteById(idTipoPago);
        return "redirect:/catalogos/tipo-pago";
    }

}
