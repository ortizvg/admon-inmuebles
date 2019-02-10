package mx.com.admoninmuebles.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

import mx.com.admoninmuebles.constant.LocaleConst;
import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.PagoBusquedaDto;
import mx.com.admoninmuebles.dto.PagoDto;
import mx.com.admoninmuebles.dto.ZonaDto;
import mx.com.admoninmuebles.persistence.model.EstatusPago;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.PagoService;
import mx.com.admoninmuebles.service.TipoPagoService;
import mx.com.admoninmuebles.service.ZonaService;

@Controller
public class PagoController {
	
	Logger logger = LoggerFactory.getLogger(PagoController.class);

    @Autowired
    private PagoService pagoService;
    
    @Autowired
    private ZonaService zonaService;
    
    @Autowired
    private InmuebleService inmuebleService;
    
    @Autowired
    private TipoPagoService tipoPagoService;
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR', 'SOCIO_BI')")
    @GetMapping(value = "/pagos")
    public String showPagos(Model model, final HttpServletRequest request) {
    	
    	 Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
    	 
    	if ( request.isUserInRole( RolConst.ROLE_SOCIO_BI ) ) {
			 model.addAttribute("pagos", pagoService.buscarPorUsuario( usuarioLogueadoId ));
        } else if ( request.isUserInRole( RolConst.ROLE_ADMIN_ZONA ) ) {
        	 ZonaDto zona = zonaService.findByAdminZonaId( usuarioLogueadoId ).stream().findFirst().get();
//        	 model.addAttribute("inmuebles", inmuebleService.findByZonaCodigo( zona.getCodigo() )  );
        	 model.addAttribute("inmuebles", inmuebleService.findByAdminZonaId( usuarioLogueadoId )  );
        	 model.addAttribute("pagos", pagoService.buscarPorCodigoZona( zona.getCodigo() ) );
        } else if ( request.isUserInRole( RolConst.ROLE_ADMIN_BI ) ) {
        	InmuebleDto inmueble = inmuebleService.findByAdminBiId( usuarioLogueadoId ).stream().findFirst().get();
        	model.addAttribute("socios", inmuebleService.findSociosByInmuebleId( inmueble.getId() ) );
       	 	model.addAttribute("pagos", pagoService.buscarPorAdminBi( inmueble.getId() ) );
        } else if ( request.isUserInRole( RolConst.ROLE_ADMIN_CORP ) ) {
        	model.addAttribute("zonas", zonaService.findAll() );
        	model.addAttribute("pagos", pagoService.buscarTodo() );
        } else if ( request.isUserInRole( RolConst.ROLE_CONTADOR ) ) {
        	model.addAttribute("pagos", pagoService.buscarPorContador(usuarioLogueadoId) );
        } else {
        	model.addAttribute("pagos", Collections.emptyList() );
        }
    	
    	
        return "pagos/pagos";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'CONTADOR', 'ADMIN_BI')")
    @GetMapping(value = "/pagos/generacion")
    public String showPagosGeneracion(final Model model, final HttpServletRequest request,  final HttpSession session, final Locale locale) {
    	
    	 Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
    	 
        if ( request.isUserInRole( RolConst.ROLE_ADMIN_ZONA ) ) {
        	 session.setAttribute("zonas", zonaService.findByAdminZonaId( usuarioLogueadoId ));
        } else if ( request.isUserInRole( RolConst.ROLE_ADMIN_BI ) ) {
        	session.setAttribute("inmuebles", inmuebleService.findByAdminBiId( usuarioLogueadoId )  );
        } else if ( request.isUserInRole( RolConst.ROLE_ADMIN_CORP ) ) {
        	session.setAttribute("zonas", zonaService.findAll() );
        } else if ( request.isUserInRole( RolConst.ROLE_CONTADOR ) ) {
        	session.setAttribute("inmuebles", inmuebleService.findByContadorId( usuarioLogueadoId )  );
        } 
    	
        session.setAttribute("tiposPago", tipoPagoService.findAllByLang( LocaleConst.LOCALE_ES ) );
        model.addAttribute("pago", new PagoDto() );
    	
        return "pagos/pago-generacion";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR', 'SOCIO_BI')")
    @GetMapping(value = "/pagos/busqueda")
    public String showPagosBusqueda(final PagoBusquedaDto pagoBusquedaDto, final Model model, final HttpServletRequest request) {
    	
    	 Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();
    	 Collection<PagoDto> pagos = Collections.emptyList() ;
    	if ( request.isUserInRole( RolConst.ROLE_SOCIO_BI ) ) {
    		pagos =  pagoService.buscarPorUsuario( usuarioLogueadoId );
        } else if ( request.isUserInRole( RolConst.ROLE_ADMIN_ZONA ) ) {
        	 
        	 if(pagoBusquedaDto.getInmuebleId() != null) {
        		 pagos =  pagoService.buscarPorInmueble( pagoBusquedaDto.getInmuebleId() ) ;
        	 } else {
        		 pagos =  pagoService.buscarPorCodigoZona( pagoBusquedaDto.getZonaCodigo() ) ;
        	 }
        } else if ( request.isUserInRole( RolConst.ROLE_ADMIN_BI ) ) {
        	
        	if( pagoBusquedaDto.getSocioId() != null ) {
        		pagos =  pagoService.buscarPorUsuario( pagoBusquedaDto.getSocioId() ) ;
        	} else if( pagoBusquedaDto.getInmuebleId() != null ) {
        		pagos = pagoService.buscarPorInmueble( pagoBusquedaDto.getInmuebleId() ) ;
        	} 
        	
        } else if ( request.isUserInRole( RolConst.ROLE_ADMIN_CORP ) ) {
        	
        	if( pagoBusquedaDto.getInmuebleId() != null ) {
        		pagos = pagoService.buscarPorInmueble( pagoBusquedaDto.getInmuebleId() ) ;
        	} else if( pagoBusquedaDto.getInmuebleId() == null && pagoBusquedaDto.getZonaCodigo() != null) {
        		pagos = pagoService.buscarPorCodigoZona( pagoBusquedaDto.getZonaCodigo() ) ;
        	} 
        	
        } 
    	
        model.addAttribute("pagos", pagos );
    	
        return "pagos/pagos";
    }
    
    @PreAuthorize("hasAnyRole('SOCIO_BI')")
    @GetMapping(value = "/pagos/{id}/opciones")
    public String showFormPagosOpciones(final @PathVariable long id, final HttpSession session, Model model) {
    	
    	if( !pagoService.existePago(id) ) {
    		return "error/404";
    	}
    	
//    	session.setAttribute("idPago", id);
    	model.addAttribute("idPago", id);
    	return "pagos/pago-opciones";
    }
    
    @GetMapping(value = "/pagos/opciones/tarjeta")
    public String showFormPagosOpcionTarjeta(Model model) {
    	return "pagos/pago-tarjeta";
    }
    
    @GetMapping(value = "/pagos/opciones/paypal")
    public String showFormPagosOpcionPaypal(Model model) {
    	return "pagos/pago-paypal";
    }
    
    @PreAuthorize("hasAnyRole('SOCIO_BI')")
    @GetMapping(value = "/pagos/{id}/opciones/transferencia")
    public String showFormPagosOpcionTransferencia(final @PathVariable long id, Model model) {
    	if( !pagoService.existePago(id) ) {
    		return "error/404";
    	}
    	
    	PagoDto pago = pagoService.buscarId(id);
    	
    	if( EstatusPago.PAGADO.equals(pago.getEstatusPagoName()) || EstatusPago.VERIFICACION.equals(pago.getEstatusPagoName()) ) {
    		return "redirect:/pagos";
    	}
    	
    	model.addAttribute("pago", pago);
    	
    	logger.info(pago.toString());
    	
    	return "pagos/pago-transferencia";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'SOCIO_BI', 'CONTADOR')")
    @GetMapping(value = "/pagos/{id}/detalle")
    public String showFormPagosDetalle(final @PathVariable long id, Model model) {
    	
    	if( !pagoService.existePago(id) ) {
    		return "error/404";
    	}
    	
    	PagoDto pago = pagoService.buscarId(id);
    	model.addAttribute("pago", pago);
    	
        return "pagos/pago-detalle";
    }
    
    @PostMapping(value = "/pagos/opciones/tarjeta")
    public String pagarTarjeta(final HttpServletRequest request, final Locale locale, final Model model, @Valid final PagoDto pagoDto, final BindingResult bindingResult) {
    	return "redirect:/pagos";
    }
    
    @PostMapping(value = "/pagos/opciones/paypal")
    public String pagarPaypal(final HttpServletRequest request, final Locale locale, final Model model, @Valid final PagoDto pagoDto, final BindingResult bindingResult) {
    	return "redirect:/pagos";
    }
    
    @PreAuthorize("hasAnyRole('SOCIO_BI')")
    @PostMapping(value = "/pagos/opciones/transferencia")
    public String pagarTransferencia(final HttpServletRequest request, final Locale locale, final Model model, @Valid final PagoDto pagoDto, final BindingResult bindingResult) {
    	
    	 if (bindingResult.hasErrors()) {
             return "pagos/pago-transferencia";
         }
    	
    	if( !pagoService.existePago( pagoDto.getId() ) ) {
    		return "error/404";
    	}
    	
    	pagoService.pagarTranferenciaBancaria(pagoDto);
    	
    	return "redirect:/pagos";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR')")
    @PostMapping(value = "/pagos/opciones/transferencia/verificacion")
    public String pagarTransferenciaVerificacion(final HttpServletRequest request, final Locale locale, final Model model, @Valid final PagoDto pagoDto, final BindingResult bindingResult) {
    	
    	if( !pagoService.existePago( pagoDto.getId() ) ) {
    		return "error/404";
    	}
    	
    	pagoService.verificar( pagoDto.getId() );
    	return "redirect:/pagos";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'CONTADOR')")
    @PostMapping(value = "/pagos/generacion")
    public String pagosGeneracion(final HttpServletRequest request, final Locale locale, final Model model, @Valid final PagoDto pagoDto, final BindingResult bindingResult) {
    	
    	 if (bindingResult.hasErrors()) {
             return "pagos/pago-generacion";
         }
    	
    	pagoService.generarPagos(pagoDto);
    	
    	return "redirect:/pagos";
    }

}
