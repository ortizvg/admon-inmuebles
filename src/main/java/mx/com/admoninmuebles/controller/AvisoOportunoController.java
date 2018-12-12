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

import mx.com.admoninmuebles.dto.AvisoOportunoDto;
import mx.com.admoninmuebles.service.AvisoOportunoService;

@Controller
public class AvisoOportunoController {

    @Autowired
    private AvisoOportunoService avisoOportunoService;

    @GetMapping(value = "/crearAvisoOportuno")
    public String showForm(final AvisoOportunoDto avisoOportunoDto) {
        return "crearAvisoOportuno";
    }

    @PostMapping(value = "/crearAvisoOportuno")
    public String crearAvisoOportuno(final Locale locale, final Model model, @Valid final AvisoOportunoDto avisoOportunoDto, final BindingResult bindingResult) {
        avisoOportunoService.save(avisoOportunoDto);
        return "redirect:/crearAvisoOportuno";
    }
    
    @GetMapping(value = "/catalogos/aviso-oportuno")
    public String init(final AvisoOportunoDto avisoOportunoDto, Model model) {
    	
    	model.addAttribute("avisosOportunos", avisoOportunoService.findAll());
    	return "catalogos/aviso-oportuno";
    }
    
    @PostMapping(value = "/catalogos/aviso-oportuno")
    public String guardarAvisoOportuno(final Locale locale, final Model model, @Valid final AvisoOportunoDto avisoOportunoDto, final BindingResult bindingResult) {
    	avisoOportunoService.save(avisoOportunoDto);
        return "redirect:/catalogos/aviso-oportuno";
    }
    
    @GetMapping(value = "/catalogos/aviso-oportuno-editar/{idAvisoOportuno}")
    public String buscarAvisoOportunoPorId(final @PathVariable long idAvisoOportuno, Model model) {
    	model.addAttribute("avisoOportuno", avisoOportunoService.findById(idAvisoOportuno));
        return "catalogos/aviso-oportuno-edicion";
    }
    
    @PostMapping(value = "/catalogos/aviso-oportuno-editar")
    public String editarAvisoOportuno(final Locale locale, final Model model, @Valid final AvisoOportunoDto avisoOportunoDto, final BindingResult bindingResult) {
    	avisoOportunoService.save(avisoOportunoDto);
        return "redirect:/catalogos/aviso-oportuno";
    }
    
    @GetMapping(value = "/catalogos/aviso-oportuno-eliminar/{idAvisoOportuno}")
    public String eliminarAvisoOportuno(final @PathVariable long idAvisoOportuno) {
    	avisoOportunoService.deleteById(idAvisoOportuno);;
        return "redirect:/catalogos/aviso-oportuno";
    }

}
