package mx.com.admoninmuebles.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mx.com.admoninmuebles.dto.TipoPagoDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.TipoPagoService;

@Controller
public class TipoPagoController {
	
	Logger logger = LoggerFactory.getLogger(PagoController.class);

    @Autowired
    private TipoPagoService tipoPagoService;
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP')")
    @GetMapping(value = "/catalogos/tipos-pago")
    public String showList(Model model) {
    	model.addAttribute("tiposPago", tipoPagoService.findAll());
        return "/catalogos/tipo-pago";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP')")
    @GetMapping(value = "/catalogos/tipos-pago/crear")
    public String showFormCreate(Model model) {
    	model.addAttribute("tipoPagoDto", new TipoPagoDto());
        return "/catalogos/tipo-pago-crear";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP')")
    @GetMapping(value = "/catalogos/tipos-pago/{id}/editar")
    public String showFormEdit(final @PathVariable long id, Model model) {
    	model.addAttribute("tipoPagoDto", tipoPagoService.findById( id ) );
        return "/catalogos/tipo-pago-edicion";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP')")
    @GetMapping(value = "/catalogos/tipos-pago/{id}/eliminar")
    public String delete(final @PathVariable long id, @RequestParam(name = "user", required = true) Long user) {
    	
        
    	Long adminCorpLogueadoId = SecurityUtils.getCurrentUserId().get();
    	if(user == null) {
    	      return "redirect:/catalogos/tipos-pago";
    	}
    	
    	if(!user.equals(adminCorpLogueadoId)) {
    	      return "redirect:/catalogos/tipos-pago";
    	}
        
		try {
			tipoPagoService.deleteById(id);
		} catch (BusinessException be) {
			 return "redirect:/catalogos/tipos-pago";
		}
		
        return "redirect:/catalogos/tipos-pago";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP')")
    @PostMapping(value = "/catalogos/tipos-pago/crear")
    public String create(final Locale locale, final Model model, @Valid final TipoPagoDto tipoPagoDto, final BindingResult bindingResult) {
        tipoPagoService.save(tipoPagoDto);
        return "redirect:/catalogos/tipos-pago";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP')")
    @PostMapping(value = "/catalogos/tipos-pago/editar")
    public String edit(final Locale locale, final Model model, @Valid final TipoPagoDto tipoPagoDto, final BindingResult bindingResult) {
        tipoPagoService.save(tipoPagoDto);
        return "redirect:/catalogos/tipos-pago";
    }
    

}
