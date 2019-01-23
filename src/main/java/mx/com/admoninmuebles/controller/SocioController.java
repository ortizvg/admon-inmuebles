package mx.com.admoninmuebles.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.CargaSocioDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.dto.ZonaDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.listener.event.OnRegistroCompletoEvent;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.CargaSocioService;
import mx.com.admoninmuebles.service.ColoniaService;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.NotificacionService;
import mx.com.admoninmuebles.service.RolService;
import mx.com.admoninmuebles.service.SocioService;
import mx.com.admoninmuebles.service.UsuarioService;
import mx.com.admoninmuebles.service.ZonaService;

@Controller
public class SocioController {
	
	Logger logger = LoggerFactory.getLogger(SocioController.class);
	
    @Autowired
    private MessageSource messages;
	
    @Autowired
    private RolService rolService;

	@Autowired
	private SocioService socioService;
	
	@Autowired
	private ZonaService zonaService;
	
	@Autowired
	private ColoniaService coloniaService;
	
	@Autowired
	private InmuebleService inmuebleService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private NotificacionService notificacionService;
	
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Autowired
    private CargaSocioService cargaSocioService;
    
    @PreAuthorize("hasAnyRole('SOCIO_BI')")
    @GetMapping(value = "/sociobi")
    public String inicioSocioBi(final Model model, final HttpSession session) {
    	Long socioBiLogueadoId = SecurityUtils.getCurrentUserId().get();
    	UsuarioDto usuarioDto = usuarioService.findById(socioBiLogueadoId);
    	model.addAttribute("socioDto", usuarioDto);
    	
    	InmuebleDto inmuebleDto = inmuebleService.findBySociosId(socioBiLogueadoId).stream().findFirst().get();
    	model.addAttribute("inmuebleDto", inmuebleDto);
    	
    	session.setAttribute("notificaciones", notificacionService.findByInmuebleIdNotExpired(inmuebleDto.getId()));
//        model.addAttribute("inmuebleDto", inmuebleService.findById(usuarioDto.getInmuebleId()));
//        session.setAttribute("notificaciones", notificacionService.findByInmuebleId(usuarioDto.getInmuebleId()));
//        session.setAttribute("notificaciones", notificacionService.findByInmuebleIdNotExpired(usuarioDto.getInmuebleId()));
        return "sociobi/inicio";
    }
    
    @PreAuthorize("hasAnyRole('REP_BI')")
    @GetMapping(value = "/repbi")
    public String inicioRepBi(final Model model) {
    	Long socioBiLogueadoId = SecurityUtils.getCurrentUserId().get();
    	UsuarioDto usuarioDto = usuarioService.findById(socioBiLogueadoId);
    	logger.info("REP NI::::: " + usuarioDto == null ? "NADA" : usuarioDto.toString());
    	
//    	InmuebleDto inmuebleDto = inmuebleService.findById(usuarioDto.getInmuebleId());
    	InmuebleDto inmuebleDto = inmuebleService.findBySociosId(socioBiLogueadoId).stream().findFirst().get();
    	
    	model.addAttribute("repDto", usuarioDto);
        model.addAttribute("inmuebleDto", inmuebleDto);
        model.addAttribute("socios", inmuebleService.findSociosByInmuebleId(inmuebleDto.getId()));
        model.addAttribute("tickets", inmuebleService.findTicketsByInmuebleId(inmuebleDto.getId()));
        return "repbi/inicio";
    }

	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
	@GetMapping(value = "/socios")
	public String init(final Model model, final HttpServletRequest request) {
		model.addAttribute("socios", socioService.getSocios());
		
		 if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
			 model.addAttribute("socios", socioService.getSocios());
             
         } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
        	 Long adminZonaLogueadoId = SecurityUtils.getCurrentUserId().get();
         	 ZonaDto zona = zonaService.findByAdminZonaId(adminZonaLogueadoId).stream().findFirst().get();
        	 model.addAttribute("socios", socioService.findSociosByZonaCodigo(zona.getCodigo()));
         
         } else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
        	 Long adminBiId = SecurityUtils.getCurrentUserId().get();
        	 model.addAttribute("socios", socioService.findSociosByAdminBiId( adminBiId ));
         }
		 
		return "socios/socios";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/socio-crear")
    public String crearSocioInit(final UsuarioDto usuarioDto, final Model model, final HttpServletRequest request, final HttpSession session) {
		session.setAttribute("rolesDto", rolService.getRolesSociosRepresentantes());
		Optional<Long> optId = SecurityUtils.getCurrentUserId();
        if (optId.isPresent()) {
            if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
                session.setAttribute("zonasDto", zonaService.findAll());
                
            } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
            	session.setAttribute("zonasDto", zonaService.findByAdminZonaId(optId.get()));
            
            } else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
            	session.setAttribute("inmueblesDto", inmuebleService.findByAdminBiId(optId.get()));
            }
        }
        return "socios/socio-crear";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PostMapping(value = "/socio-crear")
    public String crearSocio(final HttpServletRequest request, final Locale locale, final Model model, @Valid final UsuarioDto usuarioDto, final BindingResult bindingResult) {
    	logger.info(usuarioDto.toString());
        if (bindingResult.hasErrors()) {
            return "socios/socio-crear";
        }
        
        try {
	        UsuarioDto socioNuevo = (UsuarioDto) usuarioService.crearCuenta(usuarioDto);
	        inmuebleService.addSocio2Inmueble(socioNuevo, usuarioDto.getInmuebleId());
	        eventPublisher.publishEvent(new OnRegistroCompletoEvent(socioNuevo, request.getLocale(), getAppUrl(request)));
	        return "redirect:/socios";
        }catch(BusinessException e) {
   		 bindingResult.addError(new ObjectError(messages.getMessage(e.getMessage(), null, locale), messages.getMessage(e.getMessage(), null, locale)));
   		 return "socios/socio-crear";
   	 	}
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI', 'REP_BI')")
    @GetMapping(value = "/socio-detalle/{id}")
    public String buscarsocioPorId(final @PathVariable long id, final Model model) {
    	InmuebleDto inmuebleDto = inmuebleService.findBySociosId(id).stream().findFirst().get();
    	model.addAttribute("inmuebleDto", inmuebleDto);
        model.addAttribute("usuarioDto", socioService.buscarSocioPorId(id));
        return "socios/socio-detalle";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/socio-editar/{id}")
    public String editarSocio(final @PathVariable long id, final Model model, final HttpServletRequest request, final HttpSession session) {
    	UsuarioDto usuarioDto = usuarioService.findById(id);
    	InmuebleDto inmuebleDto = inmuebleService.findBySociosId(id).stream().findFirst().get();
    	usuarioDto.setInmuebleId(inmuebleDto.getId());
    	usuarioDto.setInmuebleDireccionAsentamientoZonaCodigo(usuarioDto.getInmuebleDireccionAsentamientoZonaCodigo());
    	usuarioDto.setInmuebleDireccionAsentamientoId(inmuebleDto.getDireccionAsentamientoId());
    	List<Long> rolesUsuario = usuarioDto.getRoles().stream().map(rol -> rol.getId()).collect(Collectors.toList());
    	usuarioDto.setRolSeleccionado( rolesUsuario.get(0) );
        model.addAttribute("usuarioDto", usuarioDto);
        session.setAttribute("rolesDto", rolService.getRolesSociosRepresentantes());
        
        Optional<Long> optId = SecurityUtils.getCurrentUserId();
        if (optId.isPresent()) {
            if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP) || request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
            	Collection<ZonaDto> zonasDto = request.isUserInRole(RolConst.ROLE_ADMIN_CORP) ? zonaService.findAll() : zonaService.findByAdminZonaId(optId.get());
            	session.setAttribute("zonasDto", zonasDto);
            	session.setAttribute("coloniasDto", coloniaService.findByZonaCodigo(usuarioDto.getInmuebleDireccionAsentamientoZonaCodigo()));
            	session.setAttribute("inmueblesDto", inmuebleService.findByDireccionAsentamientoId(usuarioDto.getInmuebleDireccionAsentamientoId()));
            } else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
            	session.setAttribute("inmueblesDto", inmuebleService.findByAdminBiId(optId.get()));
            }
        }
        
        logger.info(usuarioDto.toString());
        return "socios/socio-editar";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PostMapping(value = "/socio-editar")
    public String editarSocio(final Locale locale, final Model model, @Valid final UsuarioDto usuarioDto, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "socios/socio-editar";
        }

        UsuarioDto socio = usuarioService.editarCuenta(usuarioDto);
//        inmuebleService.addSocio2Inmueble(socio, usuarioDto.getInmuebleId());
        return "redirect:/socios";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/socio-eliminar/{id}")
    public String eliminarSocio(final @PathVariable Long id) {
    	usuarioService.deleteById(id);
        return "redirect:/socios";
    }
    
    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PostMapping(value = "/socio-masivo-crear")
    public String crearSocioMasivo(@RequestParam("file") MultipartFile file, RedirectAttributes redirect, Model model) {
    	String showPage = "redirect:/socio-carga-masivo";
    	final String CSV_MIME_TYPE = "text/csv";
    	final String CSV_MS_MIME_TYPE = "application/vnd.ms-excel";
        if (file.isEmpty()) {
        	redirect.addFlashAttribute("messageEmpty","");
            return showPage;
        }
        
        BufferedReader br = null;
        InputStream input = null;
        CargaSocioDto cargaSocio = null;
        try {
        	
        	String fileType = file.getContentType();
        	if(!(CSV_MIME_TYPE.contains(fileType) || CSV_MS_MIME_TYPE.contains(fileType))) {
        		redirect.addFlashAttribute("messageType","");
        		return showPage;
        	}
        	input  = file.getInputStream();
        	br = new BufferedReader(new InputStreamReader(input));
        	cargaSocio = cargaSocioService.validaCSVSocios(br);
        	if(cargaSocio!=null && cargaSocio.getListaErrores() !=null && !cargaSocio.getListaErrores().isEmpty()) {
        		model.addAttribute("errores", cargaSocio.getListaErrores());
        		return showPage.replace("redirect:", "socios");
        	}
	        return "redirect:/socios";
        }catch(IOException ie){
        	logger.error("Hubo un prolema al leer el archivo "+ie.getMessage());
        	return showPage;
        }catch(BusinessException e) {
        	return showPage;
   	 	}
    }
    
    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/socio-carga-masivo")
    public String cargarSocioMasivo(final HttpServletRequest request, final Locale locale, final Model model, @Valid final UsuarioDto usuarioDto, final BindingResult bindingResult) {
    
        try
        {}
        catch(BusinessException e) {
               bindingResult.addError(new ObjectError(messages.getMessage(e.getMessage(), null, locale), messages.getMessage(e.getMessage(), null, locale)));
               return "redirect:/socios";
               } 
        
        return "socios/socio-carga-masivo";
        
    }

}
