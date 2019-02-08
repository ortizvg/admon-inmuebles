package mx.com.admoninmuebles.controller;

import java.util.Locale;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.ProveedorDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.listener.event.OnRegistroCompletoEvent;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.AreaServicioService;
import mx.com.admoninmuebles.service.ProveedorService;
import mx.com.admoninmuebles.service.ZonaService;

@Controller
public class ProveedorController {
	
	Logger logger = LoggerFactory.getLogger(ProveedorController.class);
	
    @Autowired
    private MessageSource messages;

	@Autowired
	private ProveedorService proveedorService;
	
	@Autowired
	private AreaServicioService areaServicioService;
	
	@Autowired
	private ZonaService zonaService;
	
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
	@PreAuthorize("hasAnyRole('PROVEEDOR')")
	@GetMapping(value = "/proveedor")
	public String home(final Model model) {
		Long proveedorLogueadoId = SecurityUtils.getCurrentUserId().get();
		ProveedorDto proveedorDto = proveedorService.buscarProveedorPorId(proveedorLogueadoId);
        model.addAttribute("proveedorDto", proveedorDto);
        proveedorDto.setAreasServicioSeleccionados(proveedorDto.getAreasServicio().stream().map(as -> as.getId()).collect(Collectors.toList()));
        
        model.addAttribute("comentariosDto", proveedorDto.getComentarios());
        model.addAttribute("proveedorDto", proveedorDto);
        model.addAttribute("areasServicio", proveedorDto.getAreasServicio());
		return "proveedores/inicio";
	}

	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
	@GetMapping(value = "/proveedores")
	public String init(final Model model, final HttpServletRequest request) {
		model.addAttribute("proveedores", proveedorService.getProveedores());
		 if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
				model.addAttribute("proveedores", proveedorService.getProveedores());
             
         } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
//	     		Long adminZonaLogueadoId = SecurityUtils.getCurrentUserId().get();
//	        	ZonaDto zona = zonaService.findByAdminZonaId(adminZonaLogueadoId).stream().findFirst().get();
//        		model.addAttribute("proveedores", proveedorService.buscarProveedorPorZona(zona.getCodigo()));
        		
        		model.addAttribute("proveedores", proveedorService.getProveedores());
         
         } 
		return "proveedores/proveedores";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @GetMapping(value = "/proveedor-crear")
    public String crearProveedorInit(final ProveedorDto proveedorDto, final Model model, final HttpSession session) {
//		 model.addAttribute("areasServicio", areaServicioService.findAll());
		 session.setAttribute("areasServicio", areaServicioService.findAll());
        return "proveedores/proveedor-crear";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @PostMapping(value = "/proveedor-crear")
    public String crearProveedor(final HttpServletRequest request, final Locale locale, final Model model, @Valid final ProveedorDto proveedorDto, final BindingResult bindingResult) {
    	logger.info(proveedorDto.toString());
        if (bindingResult.hasErrors()) {
            return "proveedores/proveedor-crear";
        }
        
        try {
	        UsuarioDto proveedorNuevo = proveedorService.guardar(proveedorDto);
	        eventPublisher.publishEvent(new OnRegistroCompletoEvent(proveedorNuevo, request.getLocale(), getAppUrl(request)));
	        return "redirect:/proveedores";
        }catch(BusinessException e) {
   		 bindingResult.addError(new ObjectError(messages.getMessage(e.getMessage(), null, locale), messages.getMessage(e.getMessage(), null, locale)));
   		 return "proveedores/proveedor-crear";
   	 }
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @GetMapping(value = "/proveedor-detalle/{id}")
    public String buscarProveedorPorId(final @PathVariable long id, final Model model) {
    	ProveedorDto proveedorDto = proveedorService.buscarProveedorPorId(id);
        model.addAttribute("proveedorDto", proveedorDto);
        proveedorDto.setAreasServicioSeleccionados(proveedorDto.getAreasServicio().stream().map(as -> as.getId()).collect(Collectors.toList()));
        
        model.addAttribute("comentariosDto", proveedorDto.getComentarios());
        model.addAttribute("proveedorDto", proveedorDto);
        model.addAttribute("areasServicio", proveedorDto.getAreasServicio());
        return "proveedores/proveedor-detalle";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @GetMapping(value = "/proveedor-editar/{id}")
    public String editarProveedor(final @PathVariable long id, final Model model, final HttpSession session) {
    	ProveedorDto proveedorDto = proveedorService.buscarProveedorPorId(id);
    	proveedorDto.setAreasServicioSeleccionados(proveedorDto.getAreasServicio().stream().map(as -> as.getId()).collect(Collectors.toList()));
        model.addAttribute("proveedorDto", proveedorDto);
        model.addAttribute("areasServicio", areaServicioService.findAll());
//        model.addAttribute("coloniaDto", proveedorDto);
        return "proveedores/proveedor-editar";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @PostMapping(value = "/proveedor-editar")
    public String editarProveedor(final Locale locale, final Model model, @Valid final ProveedorDto proveedorDto, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "proveedores/proveedor-editar";
        }

        proveedorService.editar(proveedorDto);
        return "redirect:/proveedores";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @GetMapping(value = "/proveedor-eliminar/{id}")
    public String eliminarProveedor(final @PathVariable Long id) {
    	proveedorService.eliminar(id);
        return "redirect:/proveedores";
    }
    
    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
