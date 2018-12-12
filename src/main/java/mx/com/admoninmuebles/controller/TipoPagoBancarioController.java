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

import mx.com.admoninmuebles.dto.TipoPagoBancarioDto;
import mx.com.admoninmuebles.service.TipoPagoBancarioService;

@Controller
public class TipoPagoBancarioController {

    @Autowired
    private TipoPagoBancarioService tipoPagoBancarioService;

    @GetMapping(value = "/crearTipoPagoBancario")
    public String showForm(final TipoPagoBancarioDto tipoPagoBancarioDto) {
        return "crearTipoPagoBancario";
    }

    @PostMapping(value = "/crearTipoPagoBancario")
    public String crearTipoPagoBancario(final Locale locale, final Model model, @Valid final TipoPagoBancarioDto tipoPagoBancarioDto, final BindingResult bindingResult) {
        tipoPagoBancarioService.save(tipoPagoBancarioDto);
        return "redirect:/crearTipoPagoBancario";
    }
    
    @GetMapping(value = "/catalogos/tipo-pago-bancario")
    public String init(final TipoPagoBancarioDto tipoPagoBancarioDto, Model model) {
    	
    	model.addAttribute("tiposPagoBancario", tipoPagoBancarioService.findAll());
    	return "catalogos/tipo-pago-bancario";
    }
    
    @PostMapping(value = "/catalogos/tipo-pago-bancario")
    public String guardarTipoPagoBancario(final Locale locale, final Model model, @Valid final TipoPagoBancarioDto tipoPagoBancarioDto, final BindingResult bindingResult) {
    	tipoPagoBancarioService.save(tipoPagoBancarioDto);
        return "redirect:/catalogos/tipo-pago-bancario";
    }
    
    @GetMapping(value = "/catalogos/tipo-pago-bancario-editar/{idTipoPagoBancario}")
    public String buscarTipoPagoBancarioPorId(final @PathVariable long idTipoPagoBancario, Model model) {
    	model.addAttribute("tipoPagoBancarioDto", tipoPagoBancarioService.findById(idTipoPagoBancario));
        return "catalogos/tipo-pago-bancario-edicion";
    }
    
    @PostMapping(value = "/catalogos/tipo-pago-bancario-editar")
    public String editarTipoPagoBancario(final Locale locale, final Model model, @Valid final TipoPagoBancarioDto tipoPagoBancarioDto, final BindingResult bindingResult) {
    	tipoPagoBancarioService.save(tipoPagoBancarioDto);
        return "redirect:/catalogos/tipo-pago-bancario";
    }
    
    @GetMapping(value = "/catalogos/tipo-pago-bancario-eliminar/{idTipoPagoBancario}")
    public String eliminarTipoPagoBancario(final @PathVariable long idTipoPagoBancario) {
    	tipoPagoBancarioService.deleteById(idTipoPagoBancario);
        return "redirect:/catalogos/tipo-pago-bancario";
    }

}
