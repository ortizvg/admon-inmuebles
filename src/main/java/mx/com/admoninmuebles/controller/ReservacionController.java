package mx.com.admoninmuebles.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.AreaComunDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.dto.ReservacionDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.TipoPago;
import mx.com.admoninmuebles.persistence.repository.TipoPagoRepository;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.AreaComunService;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.PagoService;
import mx.com.admoninmuebles.service.ReservacionService;
import mx.com.admoninmuebles.service.ZonaService;

@Controller
public class ReservacionController {
	
	Logger logger = LoggerFactory.getLogger(ReservacionController.class);
	
	
    @Autowired
    private ReservacionService reservacioService;

    @Autowired
    private AreaComunService areaComunService;
    
	@Autowired
	private InmuebleService inmuebleService;
	
	@Autowired
	private PagoService pagoService;
	
    @Autowired
    private TipoPagoRepository tipoPagoRepository;
    
    @Autowired
    private ZonaService zonaService;
    
    @Autowired
    private MessageSource messages;

	@PreAuthorize("hasAnyRole('SOCIO_BI','ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/reservaciones/reservar-area")
    public String init(final Model model, final HttpSession session, final Locale locale,  final HttpServletRequest request) {

        Optional<Long> optUserId = SecurityUtils.getCurrentUserId();
        session.setAttribute("areasComunes", new ArrayList<AreaComunDto>());
        model.addAttribute("reservacionDto", new ReservacionDto());
        if (optUserId.isPresent()) {
            model.addAttribute("reservaciones", reservacioService.findBySocio(optUserId.get()));
            if ( request.isUserInRole( RolConst.ROLE_SOCIO_BI ) ) {
//            	 InmuebleDto inmuebleDto = inmuebleService.findBySociosId( optUserId.get() ).stream().findFirst().get();
            	 InmuebleDto inmuebleDto = inmuebleService.findBySocioId( optUserId.get() );
            	 Collection<AreaComunDto> areasComunes = areaComunService.findByInmuebleId(inmuebleDto.getId());
            	 session.setAttribute("areasComunes", areasComunes);
            } else if ( request.isUserInRole( RolConst.ROLE_ADMIN_BI ) ) {
            	session.setAttribute("inmuebles", inmuebleService.findByAdminBiId( optUserId.get() ));
        	}  else if ( request.isUserInRole( RolConst.ROLE_ADMIN_ZONA ) ) {
        		session.setAttribute("zonas", zonaService.findByAdminZonaId( optUserId.get() ));
            }  else if ( request.isUserInRole( RolConst.ROLE_ADMIN_CORP ) ) {
            	session.setAttribute("zonas", zonaService.findAll() );
            } 
        }
        session.setAttribute("locale", locale.getLanguage());
        return "/reservaciones/reservar-area-comun";
    }
	
	@PreAuthorize("hasAnyRole('SOCIO_BI', 'ADMIN_BI')")
    @GetMapping(value = "/reservaciones/mis-reservaciones")
    public String misReservaciones(final Model model, final HttpSession session, final ReservacionDto reservacionDto, final Locale locale,  final HttpServletRequest request) {

        Optional<Long> optUserId = SecurityUtils.getCurrentUserId();
        
        if (optUserId.isPresent()) {
            model.addAttribute("reservaciones", reservacioService.findBySocio(optUserId.get()));
        }
        session.setAttribute("locale", locale.getLanguage());
        return "/reservaciones/mis-reservaciones";
    }

    @PreAuthorize("hasAnyRole('SOCIO_BI','ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PostMapping(value = "/reservaciones/reservar-area/busqueda")
    public String buscarReservaciones(final Model model, final HttpSession session, final ReservacionDto reservacionDto, final Locale locale, final HttpServletRequest request) {
        Optional<Long> optUserId = SecurityUtils.getCurrentUserId();
        session.setAttribute("locale", locale.getLanguage());
        if (null != reservacionDto.getAreaComunId()) {
            model.addAttribute("reservaciones", reservacioService.findByAreaComun(reservacionDto.getAreaComunId()));
        } else if ( request.isUserInRole( RolConst.ROLE_SOCIO_BI ) ) {
            model.addAttribute("reservaciones", reservacioService.findBySocio(optUserId.get()));
        }
        session.setAttribute("areaComunId", reservacionDto.getAreaComunId());
        return "/reservaciones/reservar-area-comun";
    }

    @PreAuthorize("hasAnyRole('SOCIO_BI')")
    @PostMapping(value = "/reservaciones/reserva-area/crear")
    public String reservarReservacion(final HttpSession session, final ReservacionDto reservacionDto, final Locale locale, RedirectAttributes redirect) {
    	session.setAttribute("locale", locale.getLanguage());
    	Optional<Long> optUserId = SecurityUtils.getCurrentUserId();
    	reservacionDto.setSocioId(SecurityUtils.getCurrentUserId().get());
    	reservacionDto.setAreaComunId((Long) session.getAttribute("areaComunId"));
		
    	try {
    		
    		reservacioService.validateReservation(reservacionDto);
    	} catch (BusinessException e) {
    		redirect.addFlashAttribute("message", messages.getMessage(e.getMessage(), null, locale));
            return "redirect:/reservaciones/reservar-area";
    	}
    	
		AreaComunDto areaComunDto = areaComunService.findById((Long) session.getAttribute("areaComunId"));
		TipoPago tipoPagoReservaAreaComun = tipoPagoRepository.findByNameAndLang( TipoPago.RESERVA, locale.getLanguage() );
    	
    	PagoDto pagoDto = new PagoDto();
    	pagoDto.setUsuarioId(optUserId.get());
    	pagoDto.setTipoPagoId(tipoPagoReservaAreaComun.getId());
    	pagoDto.setMonto(areaComunDto.getCuotaPorDia());
    	pagoDto.setConcepto(reservacionDto.getTitle());
    	PagoDto pagoNuevo = pagoService.generarPagosPorSocio(pagoDto);
    	
        reservacionDto.setPagoId(pagoNuevo.getId());
        
        
        reservacioService.save(reservacionDto);
        redirect.addFlashAttribute("showAlert", "");
        return "redirect:/reservaciones/reservar-area";
    }
    
    @PreAuthorize("hasAnyRole( 'ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI' )")
    @PostMapping(value = "/reservaciones/reserva-area/eliminar")
    public String eliminarReservacion(final HttpSession session, final ReservacionDto reservacionDto, final Locale locale) {
    	ReservacionDto reservacion = reservacioService.findById(reservacionDto.getId());
    	pagoService. eliminarPorId(reservacion.getPagoId());
        reservacioService.delete(reservacionDto.getId());
        return "redirect:/reservaciones/reservar-area";
    }

}
