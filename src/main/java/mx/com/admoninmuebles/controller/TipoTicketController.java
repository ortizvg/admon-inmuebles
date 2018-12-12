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

import mx.com.admoninmuebles.dto.TipoTicketDto;
import mx.com.admoninmuebles.service.TipoTicketService;

@Controller
public class TipoTicketController {

    @Autowired
    private TipoTicketService tipoTicketService;

    @GetMapping(value = "/crearTipoTicket")
    public String showForm(final TipoTicketDto tipoTicketDto) {
        return "crearTipoTicket";
    }

    @PostMapping(value = "/crearTipoTicket")
    public String crearTipoTicket(final Locale locale, final Model model, @Valid final TipoTicketDto tipoTicketDto, final BindingResult bindingResult) {
        tipoTicketService.save(tipoTicketDto);
        return "redirect:/crearTipoTicket";
    }
    
    @GetMapping(value = "/catalogos/tipo-ticket")
    public String init(final TipoTicketDto tipoTicketDto, Model model) {
    	
    	model.addAttribute("tiposTicket", tipoTicketService.findAll());
    	return "catalogos/tipo-ticket";
    }
    
    @PostMapping(value = "/catalogos/tipo-ticket")
    public String guardarTipoTicket(final Locale locale, final Model model, @Valid final TipoTicketDto tipoTicketDto, final BindingResult bindingResult) {
    	tipoTicketService.save(tipoTicketDto);
        return "redirect:/catalogos/tipo-ticket";
    }
    
    @GetMapping(value = "/catalogos/tipo-ticket-editar/{idTipoTicket}")
    public String buscarTipoTicketPorId(final @PathVariable long idTipoTicket, Model model) {
    	model.addAttribute("tipoTicketDto", tipoTicketService.findById(idTipoTicket));
        return "catalogos/tipo-ticket-edicion";
    }
    
    @PostMapping(value = "/catalogos/tipo-ticket-editar")
    public String editarTipoTicket(final Locale locale, final Model model, @Valid final TipoTicketDto tipoTicketDto, final BindingResult bindingResult) {
    	tipoTicketService.save(tipoTicketDto);
        return "redirect:/catalogos/tipo-ticket";
    }
    
    @GetMapping(value = "/catalogos/tipo-ticket-eliminar/{idTipoTicket}")
    public String eliminarTipoTicket(final @PathVariable long idTipoTicket) {
    	tipoTicketService.deleteById(idTipoTicket);
        return "redirect:/catalogos/tipo-ticket";
    }

}
