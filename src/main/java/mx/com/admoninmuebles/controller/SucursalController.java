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

import mx.com.admoninmuebles.dto.SucursalDto;
import mx.com.admoninmuebles.service.SucursalService;

@Controller
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    
    @GetMapping(value = "/catalogos/sucursales")
    public String init(Model model) {
    	model.addAttribute("sucursalesDto", sucursalService.findAll());
    	return "catalogos/sucursales";
    }
    
    @GetMapping(value = "/catalogos/sucursal-crear")
    public String init(final SucursalDto sucursalDto) {
    	return "catalogos/sucursal-crear";
    }
    
    @PostMapping(value = "/catalogos/sucursal-crear")
    public String guardarSucursal(final Locale locale, final Model model, @Valid final SucursalDto sucursalDto, final BindingResult bindingResult) {
    	if (bindingResult.hasErrors()) {
            return "catalogos/inmueble-crear";
        }
    	sucursalService.save(sucursalDto);
        return "redirect:/catalogos/sucursales";
    }
    
    @GetMapping(value = "/catalogos/sucursal-editar/{idSucursal}")
    public String editarSucursal(final @PathVariable long idSucursal, Model model) {
    	model.addAttribute("sucursalDto", sucursalService.findById(idSucursal));
        return "catalogos/sucursal-edicion";
    }
    
    @PostMapping(value = "/catalogos/sucursal-editar")
    public String editarSucursal(final Locale locale, final Model model, @Valid final SucursalDto sucursalDto, final BindingResult bindingResult) {
    	if (bindingResult.hasErrors()) {
            return "catalogos/sucursal-edicion";
        }
    	sucursalService.save(sucursalDto);
        return "redirect:/catalogos/sucursales";
    }
    
    @GetMapping(value = "/catalogos/sucursal-detalle/{idSucursal}")
    public String verSucursal(final @PathVariable long idSucursal, Model model) {
    	model.addAttribute("sucursalDto", sucursalService.findById(idSucursal));
        return "catalogos/sucursal-detalle";
    }
    
    @GetMapping(value = "/catalogos/sucursal-eliminar/{idSucursal}")
    public String eliminarSucursal(final @PathVariable long idSucursal) {
    	sucursalService.deleteById(idSucursal);
        return "redirect:/catalogos/sucursales";
    }

}
