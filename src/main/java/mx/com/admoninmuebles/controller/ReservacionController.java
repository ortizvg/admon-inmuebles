package mx.com.admoninmuebles.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.AreaComunDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.ReservacionDto;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.AreaComunService;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.ReservacionService;

@Controller
public class ReservacionController {
    @Autowired
    private ReservacionService reservacioService;

    @Autowired
    private AreaComunService areaComunService;
    
	@Autowired
	private InmuebleService inmuebleService;

	@PreAuthorize("hasAnyRole('SOCIO_BI','ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/reservar-area-comun")
    public String init(final Model model, final HttpSession session, final ReservacionDto reservacionDto, final Locale locale,  final HttpServletRequest request) {

        Optional<Long> optUserId = SecurityUtils.getCurrentUserId();
        
        if (optUserId.isPresent()) {
            model.addAttribute("reservaciones", reservacioService.findBySocio(optUserId.get()));
            if (request.isUserInRole( RolConst.ROLE_SOCIO_BI ) || request.isUserInRole( RolConst.ROLE_SOCIO_BI ) ) {
            	InmuebleDto inmuebleDto = inmuebleService.findBySociosId( optUserId.get() ).stream().findFirst().get();
            	 Collection<AreaComunDto> areasComunes = areaComunService.findByInmuebleId(inmuebleDto.getId());
            	 session.setAttribute("areasComunes", areasComunes);
            } else {
            	session.setAttribute("areasComunes", Collections.emptyList());
            }
        }
        session.setAttribute("locale", locale.getLanguage());
        return "/reservaciones/reservar-area-comun";
    }

    @PreAuthorize("hasAnyRole('SOCIO_BI','ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PostMapping(value = "/reserva-area-comun/busqueda")
    public String buscarReservaciones(final Model model, final HttpSession session, final ReservacionDto reservacionDto, final Locale locale) {
        Optional<Long> optUserId = SecurityUtils.getCurrentUserId();
        session.setAttribute("locale", locale.getLanguage());
        if (null != reservacionDto.getAreaComunId()) {
            model.addAttribute("reservaciones", reservacioService.findByAreaComun(reservacionDto.getAreaComunId()));
        } else {
            model.addAttribute("reservaciones", reservacioService.findBySocio(optUserId.get()));
        }
        session.setAttribute("areaComunId", reservacionDto.getAreaComunId());
        return "/reservaciones/reservar-area-comun";
    }

    @PreAuthorize("hasAnyRole('SOCIO_BI')")
    @PostMapping(value = "/reserva-area-comun/crear")
    public String reservarReservacion(final HttpSession session, final ReservacionDto reservacionDto, final Locale locale) {
    	session.setAttribute("locale", locale.getLanguage());
        reservacionDto.setSocioId(SecurityUtils.getCurrentUserId().get());
        reservacionDto.setAreaComunId((Long) session.getAttribute("areaComunId"));
        reservacioService.save(reservacionDto);
        return "redirect:/reservar-area-comun";
    }
    
    @PreAuthorize("hasAnyRole('SOCIO_BI')")
    @PostMapping(value = "/reserva-area-comun/eliminar")
    public String eliminarReservacion(final HttpSession session, final ReservacionDto reservacionDto, final Locale locale) {
        reservacioService.delete(reservacionDto.getId());
        return "redirect:/reservar-area-comun";
    }

}
