package mx.com.admoninmuebles.controller;

import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.com.admoninmuebles.constant.LocaleConst;
import mx.com.admoninmuebles.dto.MensajeContactoDto;
import mx.com.admoninmuebles.persistence.repository.EstadoCorreoRepository;
import mx.com.admoninmuebles.persistence.repository.EstadoRepository;
import mx.com.admoninmuebles.service.EstadoCorreoService;
import mx.com.admoninmuebles.service.MensajeContactoEstatusService;
import mx.com.admoninmuebles.service.MensajeContactoService;
import mx.com.admoninmuebles.service.SectorService;
import mx.com.admoninmuebles.service.ZonaService;

@Controller
public class MensajeContactoController {
	
	private final static Long MENSAJE_CONTACTO_ESTATUS_NO_ATENDIDO_ES = 1l;
	private final static Long MENSAJE_CONTACTO_ESTATUS_NO_ATENDIDO_EN = 4l;
	
    @Autowired
    private MessageSource messages;

    @Autowired
    private MensajeContactoService mensajeContactoService;
    
    @Autowired
    private MensajeContactoEstatusService mensajeContactoEstatusService;
    
    @Autowired
    private SectorService sectorService;
    
    @Autowired
    private ZonaService zonaService;
    
    @Autowired
    private EstadoCorreoService estadoCorreoService;

    
    @RequestMapping("/contacto")
    public String contacto(final Locale locale, final MensajeContactoDto mensajeContactoDto, final HttpSession session) {
    	session.setAttribute("sectoresDto", sectorService.findByIdioma(locale.getLanguage()));
    	session.setAttribute("zonasDto", zonaService.findAll());
    	session.setAttribute("estadosDto", estadoCorreoService.findAll());
        return "/contacto/contacto";
    }

    @PostMapping(value = "/contacto")
    public String contactoMensajeCrear(final HttpSession session, final Locale locale, final Model model, @Valid final MensajeContactoDto mensajeContactoDto, final BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    	System.out.println("DATA " + mensajeContactoDto.toString());
    	if (bindingResult.hasErrors()) {
    		session.setAttribute("sectoresDto", sectorService.findByIdioma(locale.getLanguage()));
        	session.setAttribute("zonasDto", zonaService.findAll());
        	session.setAttribute("estadosDto", estadoCorreoService.findAll());
        	redirectAttributes.addFlashAttribute("message", messages.getMessage("contacto.guarda.error", null, locale));
            return "/contacto/contacto";
        }
        redirectAttributes.addFlashAttribute("message", messages.getMessage("contacto.guarda.ok", null, locale));
        
        mensajeContactoDto.setMensajeContactoEstatusId(MENSAJE_CONTACTO_ESTATUS_NO_ATENDIDO_ES);
        if(LocaleConst.LOCALE_EN.equalsIgnoreCase(locale.getLanguage())) {
        	mensajeContactoDto.setMensajeContactoEstatusId(MENSAJE_CONTACTO_ESTATUS_NO_ATENDIDO_EN);
        }
        mensajeContactoService.save(mensajeContactoDto, locale);
        return "redirect:/contacto";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @RequestMapping("/contacto/mensajes")
    public String contactoMensajes(final Locale locale, final Model model) {
    	model.addAttribute("mensajesContactoDto", mensajeContactoService.findAll());
        return "/contacto/contacto-mensajes";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @RequestMapping("/contacto/mensaje-detalle/{id}")
    public String contactoMensajesDetalle(final @PathVariable long id, final Model model, final HttpSession session) {
//    	session.setAttribute("mensajesContactoEstatusDto", mensajeContactoEstatusService.findAll());
    	model.addAttribute("mensajeContactoDto", mensajeContactoService.findById(id));
        return "/contacto/contacto-mensaje-detalle";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @RequestMapping("/contacto/mensaje-atender/{id}")
    public String contactoMensajesAtender(final Locale locale, final @PathVariable long id, final Model model, final HttpSession session) {
    	
    	session.setAttribute("mensajesContactoEstatusDto", mensajeContactoEstatusService.findByIdioma(locale.getLanguage()));
    	model.addAttribute("mensajeContactoDto", mensajeContactoService.findById(id));
        return "/contacto/contacto-mensaje-atender";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PostMapping(value = "/contacto/mensaje-atender")
    public String contactoMensajesAtender(final Locale locale, final Model model, @Valid final MensajeContactoDto mensajeContactoDto, final BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/contacto/contacto-mensaje-atender";
        }
//        redirectAttributes.addFlashAttribute("message", messages.getMessage("contacto.atendido.ok", null, locale));
        mensajeContactoService.update(mensajeContactoDto);
        return "redirect:/contacto/mensajes";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @RequestMapping("/contacto/mensaje-eliminar/{id}")
    public String contactoMensajesEliminar(final @PathVariable long id) {
    	mensajeContactoService.deleteById(id);
        return "redirect:/contacto/mensajes";
    }

}
