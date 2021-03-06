package mx.com.admoninmuebles.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.com.admoninmuebles.constant.ComunConst;
import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.ColoniaDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.ZonaDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.ColoniaService;
import mx.com.admoninmuebles.service.ContadorService;
import mx.com.admoninmuebles.service.InmuebleService;
import mx.com.admoninmuebles.service.UsuarioService;
import mx.com.admoninmuebles.service.ZonaService;
import mx.com.admoninmuebles.storage.StorageService;
import mx.com.admoninmuebles.validation.MimeTypeValidator;

@Controller
public class InmuebleController {
	
	Logger logger = LoggerFactory.getLogger(InmuebleController.class);
	
    @Autowired
    private StorageService storageService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ColoniaService coloniaService;
    @Autowired
    private ZonaService zonaService;
    @Autowired
    private ContadorService contadorService;

    @Autowired
    private InmuebleService inmuebleService;
    
    @Autowired
    private MessageSource messages;

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/inmuebles")
    public String init(final InmuebleDto inmuebleDto, final Model model, final HttpServletRequest request) {
        Optional<Long> optUsuarioId = SecurityUtils.getCurrentUserId();
        Collection<ZonaDto> zonas = Collections.emptyList();
        model.addAttribute("inmuebles", Collections.emptyList());
        if (optUsuarioId.isPresent()) {
            if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
                zonas = zonaService.findAll();
                model.addAttribute("inmuebles", inmuebleService.findAll() );
            } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
                zonas = zonaService.findByAdminZonaId(optUsuarioId.get());
                model.addAttribute("inmuebles", inmuebleService.findByAdminZonaId( optUsuarioId.get() ) );
            } else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
                zonas = zonaService.findByAdministradoresBiId(optUsuarioId.get());
                model.addAttribute("inmuebles", inmuebleService.findByAdminBiId( optUsuarioId.get() ) );
            }
        }
        model.addAttribute("zonas", zonas);
        
        if (null != inmuebleDto.getDireccionAsentamientoId() ) {
        	 model.addAttribute("inmuebles", inmuebleService.findByDireccionAsentamientoId(inmuebleDto.getDireccionAsentamientoId()));
        } else if( null != inmuebleDto.getZonaCodigo() ) {
        	 model.addAttribute("inmuebles", inmuebleService.findByZonaCodigo( inmuebleDto.getZonaCodigo() ));
        }

//        if (null != inmuebleDto.getZonaCodigo()) {
//            Collection<ColoniaDto> colonias = coloniaService.findByZonaCodigo(inmuebleDto.getZonaCodigo());
//            model.addAttribute("colonias", colonias);
//            if (null != inmuebleDto.getDireccionAsentamientoId()) {
//                model.addAttribute("inmuebles", inmuebleService.findByDireccionAsentamientoId(inmuebleDto.getDireccionAsentamientoId()));
//                return "inmuebles/inmuebles";
//            }
//        }
//
//        if (1 == zonas.size()) {
//            String zonaCodigo = zonas.iterator().next().getCodigo();
//            inmuebleDto.setZonaCodigo(zonaCodigo);
//            Collection<ColoniaDto> colonias = coloniaService.findByZonaCodigo(zonaCodigo);
//            model.addAttribute("colonias", colonias);
//            if (1 == colonias.size()) {
//                inmuebleDto.setDireccionAsentamientoId(colonias.iterator().next().getId());
//                model.addAttribute("inmuebles", inmuebleService.findByDireccionAsentamientoId(inmuebleDto.getDireccionAsentamientoId()));
//            }
//
//        }
        
        return "inmuebles/inmuebles";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/inmueble-crear")
    public String crearInmueble(final InmuebleDto inmuebleDto, final Model model, final HttpServletRequest request) {

        Optional<Long> optUsuarioId = SecurityUtils.getCurrentUserId();
        Collection<ZonaDto> zonas = Collections.emptyList();
        if (optUsuarioId.isPresent()) {
            if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
                zonas = zonaService.findAll();
            } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
                zonas = zonaService.findByAdminZonaId(optUsuarioId.get());
            } else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
                zonas = zonaService.findByAdministradoresBiId(optUsuarioId.get());
                inmuebleDto.setAdminBiId(optUsuarioId.get());
            }
        }
        if (1 == zonas.size()) {
            String zonaCodigo = zonas.iterator().next().getCodigo();
            inmuebleDto.setZonaCodigo(zonaCodigo);
            Collection<ColoniaDto> colonias = coloniaService.findByZonaCodigo(zonaCodigo);
            model.addAttribute("colonias", colonias);
            if (1 == colonias.size()) {
                inmuebleDto.setDireccionAsentamientoId(colonias.iterator().next().getId());
            }
            model.addAttribute("administradoresBi", usuarioService.findAdministradoresBiByZonaCodigo(zonaCodigo));
        }
        model.addAttribute("contadores", contadorService.buscarTodos());
        model.addAttribute("zonas", zonas);

        return "inmuebles/inmueble-crear";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PostMapping(value = "/inmueble-crear")
    public String guardarInmueble(final Locale locale, final Model model, @Valid final InmuebleDto inmuebleDto, final BindingResult bindingResult, RedirectAttributes redirect) {
    	logger.info(inmuebleDto.toString());
        if (bindingResult.hasErrors()) {
            return "inmuebles/inmueble-crear";
        }
        
        if( !MimeTypeValidator.isImage( inmuebleDto.getImagen().getContentType() ) ) {
        	 redirect.addFlashAttribute("error",   messages.getMessage("mensaje.inmueble.imagen.validacion", null, locale) );
        	 return "redirect:/inmueble-crear";
        }
        
        if( inmuebleDto.getImagen().getSize() > ComunConst.TAMANIO_5_MB ) {
        	 redirect.addFlashAttribute("error",   messages.getMessage("mensaje.inmueble.imagen.validacion.tamanio", null, locale) );
        	 return "redirect:/inmueble-crear";
		}
        
        inmuebleDto.setImagenUrl("/" + storageService.store(inmuebleDto.getImagen()));
        inmuebleService.save(inmuebleDto);

        return "redirect:inmuebles";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "inmueble-detalle/{id}")
    public String buscarInmueblePorId(final @PathVariable long id, final Model model) {
        model.addAttribute("inmuebleDto", inmuebleService.findById(id));
        return "inmuebles/inmueble-detalle";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "inmueble-editar/{id}")
    public String editarInmueblePorId(final @PathVariable long id, final Model model, final HttpServletRequest request) {
        InmuebleDto inmuebleDto = inmuebleService.findById(id);
        model.addAttribute("inmuebleDto", inmuebleDto);
        Optional<Long> optUsuarioId = SecurityUtils.getCurrentUserId();
        Collection<ZonaDto> zonas = Collections.emptyList();
        if (optUsuarioId.isPresent()) {
            if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
                zonas = zonaService.findAll();
            } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
                zonas = zonaService.findByAdminZonaId(optUsuarioId.get());
            } else if (request.isUserInRole(RolConst.ROLE_ADMIN_BI)) {
                zonas = zonaService.findByAdministradoresBiId(optUsuarioId.get());
            }
        }
        String zonaCodigo = coloniaService.findById(inmuebleDto.getDireccionAsentamientoId()).getZonaCodigo();
        inmuebleDto.setZonaCodigo(zonaCodigo);
        Collection<ColoniaDto> colonias = coloniaService.findByZonaCodigo(zonaCodigo);
        model.addAttribute("colonias", colonias);
        model.addAttribute("administradoresBi", usuarioService.findAdministradoresBiByZonaCodigo(zonaCodigo));
        model.addAttribute("zonas", zonas);
        model.addAttribute("contadores", contadorService.buscarTodos());
        return "inmuebles/inmueble-editar";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @PostMapping(value = "/inmueble-editar")
    public String editarInmueble(final Locale locale, final Model model, @Valid final InmuebleDto inmuebleDto, final BindingResult bindingResult, RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) {
            return "inmuebles/inmueble-editar";
        }
        
        if( !MimeTypeValidator.isImage( inmuebleDto.getImagen().getContentType() ) ) {
       	 redirect.addFlashAttribute("error",   messages.getMessage("mensaje.inmueble.imagen.validacion", null, locale) );
       	 return "redirect:/inmueble-editar";
       }

        if (StringUtils.isEmpty(inmuebleDto.getImagenUrl())) {
            inmuebleDto.setImagenUrl("/" + storageService.store(inmuebleDto.getImagen()));
        }
        inmuebleService.update(inmuebleDto);
        return "redirect:/inmuebles";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA', 'ADMIN_BI')")
    @GetMapping(value = "/inmueble-eliminar/{id}")
    public String eliminarInmueble(final @PathVariable Long id) {
        inmuebleService.deleteById(id);
        return "redirect:/inmuebles";
    }
}
