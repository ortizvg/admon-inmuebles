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

import mx.com.admoninmuebles.dto.EstatusTicketDto;
import mx.com.admoninmuebles.service.EstatusTicketService;

@Controller
public class EstatusTicketController {

    @Autowired
    private EstatusTicketService estatusTicketService;

    @GetMapping(value = "/crearEstatusTicket")
    public String showForm(final EstatusTicketDto estatusTicketDto) {
        return "crearEstatusTicket";
    }

    @PostMapping(value = "/crearEstatusTicket")
    public String crearEstatusTicket(final Locale locale, final Model model, @Valid final EstatusTicketDto estatusTicketDto, final BindingResult bindingResult) {
        estatusTicketService.save(estatusTicketDto);
        return "redirect:/crearEstatusTicket";
    }
    
    @PostMapping(value = "/catalogos/estatus-ticket")
    public String guardarAreaServicio(final Locale locale, final Model model, @Valid final EstatusTicketDto estatusTicketDto, final BindingResult bindingResult) {
    	estatusTicketService.save(estatusTicketDto);
        return "redirect:/catalogos/estatus-ticket";
    }
    
    @GetMapping(value = "/catalogos/estatus-ticket")
    public String init(final EstatusTicketDto estatusTicketDto, Model model) {
    	
    	model.addAttribute("estatusTickets", estatusTicketService.findAll());
        return "catalogos/estatus-ticket";
    }
    
    @GetMapping(value = "/catalogos/estatus-ticket-edicion/{idEstatusTicket}")
    public String buscarEstatusTicketPorId(final @PathVariable long idEstatusTicket, Model model) {
    	
    	model.addAttribute("estatusTicketDto", estatusTicketService.findById(idEstatusTicket));
        return "catalogos/estatus-ticket-edicion";
    }
    
    @PostMapping(value = "/catalogos/editar-estatus-ticket")
    public String editarAreaServicio(final Locale locale, final Model model, @Valid final EstatusTicketDto estatusTicketDto, final BindingResult bindingResult) {
    	estatusTicketService.save(estatusTicketDto);
        return "redirect:/catalogos/estatus-ticket";
    }
    
    @GetMapping(value = "/catalogos/eliminar-estatus-ticket/{idEstatusTicket}")
    public String eliminarAreaServicio(final @PathVariable long idEstatusTicket) {
    	estatusTicketService.deleteById(idEstatusTicket);;
        return "redirect:/catalogos/estatus-ticket";
    }


}
