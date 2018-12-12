package mx.com.admoninmuebles.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.admoninmuebles.dto.PreguntaFrecuenteDto;
import mx.com.admoninmuebles.service.PreguntaFrecuenteService;

@Controller
public class PreguntaFrecuenteController {

    @Autowired
    private PreguntaFrecuenteService preguntaFrecuenteService;
    
    @GetMapping(value = "/preguntas-frecuentes")
    public String mostrar(final Locale locale, Model model) {
    	
    	model.addAttribute("preguntasFrecuentesDto", preguntaFrecuenteService.findByIdioma(locale.getLanguage()));
    	return "preguntasfrecuentes/preguntas-frecuentes";
    }

    
    @GetMapping(value = "/catalogos/preguntas-frecuentes")
    public String init(final Locale locale, final PreguntaFrecuenteDto preguntaFrecuenteDto, Model model) {
    	
    	model.addAttribute("preguntasFrecuentes", preguntaFrecuenteService.findByIdioma(locale.getLanguage()));
    	return "catalogos/preguntas-frecuentes";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/preguntas-frecuentes-crear")
    public String guardarPreguntasFrecuentes(Model model, final PreguntaFrecuenteDto preguntaFrecuenteDto) {
//    	model.addAttribute("preguntaFrecuenteDto", preguntaFrecuenteService.findById(idPreguntaFrecuente));
        return "catalogos/preguntas-frecuentes-crear";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PostMapping(value = "/catalogos/preguntas-frecuentes-crear")
    public String guardarPreguntasFrecuentes(final Locale locale, final Model model, @Valid final PreguntaFrecuenteDto preguntaFrecuenteDto, final BindingResult bindingResult) {
   	 if (bindingResult.hasErrors()) {
         return "catalogos/preguntas-frecuentes-crear";
     }
    	preguntaFrecuenteDto.setIdioma(locale.getLanguage());
    	preguntaFrecuenteService.save(preguntaFrecuenteDto);
        return "redirect:/catalogos/preguntas-frecuentes";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/preguntas-frecuentes-editar/{id}")
    public String buscarPreguntasFrecuentesPorId(final Locale locale, final @PathVariable long id, Model model) {
    	model.addAttribute("preguntaFrecuenteDto", preguntaFrecuenteService.findById(id));
        return "catalogos/preguntas-frecuentes-edicion";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PostMapping(value = "/catalogos/preguntas-frecuentes-editar")
    public String editarPreguntasFrecuentes(final Locale locale, final Model model, @Valid final PreguntaFrecuenteDto preguntaFrecuenteDto, final BindingResult bindingResult) {
    	preguntaFrecuenteDto.setIdioma(locale.getLanguage());
    	preguntaFrecuenteService.save(preguntaFrecuenteDto);
        return "redirect:/catalogos/preguntas-frecuentes";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/catalogos/preguntas-frecuentes-eliminar/{idPreguntaFrecuente}")
    public String eliminarPreguntasFrecuentes(final @PathVariable long idPreguntaFrecuente) {
    	preguntaFrecuenteService.deleteById(idPreguntaFrecuente);;
        return "redirect:/catalogos/preguntas-frecuentes";
    }

}
