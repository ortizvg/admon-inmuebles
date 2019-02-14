package mx.com.admoninmuebles.controller;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.dto.ColoniaDto;
import mx.com.admoninmuebles.security.SecurityUtils;
import mx.com.admoninmuebles.service.ColoniaService;
import mx.com.admoninmuebles.service.ZonaService;

@Controller
public class ColoniaController {

    @Autowired
    private ColoniaService coloniaService;

    @Autowired
    private ZonaService zonaService;

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @GetMapping(value = "/catalogos/colonias")
    public String init(final ColoniaDto coloniaDto, final Model model, final HttpServletRequest request) {
    	System.out.println("Inicio");
    	URL urlFolderReportes = Thread.currentThread().getContextClassLoader().getResource("gesco");
		URL urlFolderReportesSlash = Thread.currentThread().getContextClassLoader().getResource("/gesco");
		InputStream inputStream1 = ColoniaController.class.getResourceAsStream("/gesco");
		InputStream inputStream2 = getClass().getClassLoader().getResourceAsStream("gesco");
		
		try {
			System.out.println( urlFolderReportes.getPath());
			System.out.println( urlFolderReportes.toURI().getPath());
		} catch (URISyntaxException e) {
			System.out.println("TRONO");
			e.printStackTrace();
		}
    	
    	Long usuarioLogueadoId = SecurityUtils.getCurrentUserId().get();

		 if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
			 model.addAttribute("colonias", coloniaService.findByZonaIsNotNull());
            
        } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
//	         ZonaDto zona = zonaService.findByAdminZonaId(usuarioLogueadoId).stream().findFirst().get();
//        	 model.addAttribute("colonias", coloniaService.findByZonaCodigo(zona.getCodigo()));
        	 model.addAttribute("colonias", coloniaService.findByAdminZona( usuarioLogueadoId) );
        } 
        return "catalogos/colonias";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @GetMapping(value = "/catalogos/colonia-agregar")
    public String agregarColonia(final ColoniaDto coloniaDto, final HttpSession session, final HttpServletRequest request) {
    	Optional<Long> optId = SecurityUtils.getCurrentUserId();
    	
        if (request.isUserInRole(RolConst.ROLE_ADMIN_CORP)) {
            session.setAttribute("zonas", zonaService.findAll());
            
        } else if (request.isUserInRole(RolConst.ROLE_ADMIN_ZONA)) {
        	session.setAttribute("zonas", zonaService.findByAdminZonaId(optId.get()));
        
        }
        return "catalogos/colonia-agregar";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @PostMapping(value = "/catalogos/colonia-agregar", params = { "buscar" })
    public String buscarColonias(@Valid final ColoniaDto coloniaDto, final HttpSession session, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "catalogos/colonia-agregar";
        }
        session.setAttribute("colonias", coloniaService.findBycodigoPostal(coloniaDto.getCodigoPostal()));
        return "catalogos/colonia-agregar";
    }

    @PreAuthorize("hasAnyRole('ADMIN_CORP', 'ADMIN_ZONA')")
    @PostMapping(value = "/catalogos/colonia-agregar")
    public String guardar(@Valid final ColoniaDto coloniaDto, final HttpSession session, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "catalogos/colonia-agregar";
        }
        coloniaService.save(coloniaDto);

        session.setAttribute("colonias", coloniaService.findBycodigoPostal(coloniaDto.getCodigoPostal()));
        return "redirect:/catalogos/colonias";
    }
    
    @PreAuthorize("hasRole('ADMIN_CORP')")
    @GetMapping(value = "/catalogos/colonia-eliminar/{id}")
    public String eliminarZona(final @PathVariable Long id) {
    	coloniaService.deleteById(id);
        return "redirect:/catalogos/colonias";
    }

}
