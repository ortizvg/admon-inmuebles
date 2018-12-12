
package mx.com.admoninmuebles.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String root() {
        return "redirect:/inicio";
    }

    @RequestMapping("/inicio")
    public String inicio() {
        return "inicio";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/informacion/nuestros-clientes")
    public String nuestrosClientes() {
        return "/informacion/nuestros-clientes";
    }

    @RequestMapping("/informacion/quienes-somos")
    public String quienesSomos() {
        return "/informacion/quienes-somos";
    }

    @RequestMapping("/informacion/servicios")
    public String servicios() {
        return "/informacion/servicios";
    }

    @RequestMapping("/informacion/ventajas-competitivas")
    public String ventajasCompetitivas() {
        return "/informacion/ventajas-competitivas";
    }

    @RequestMapping(value = "/acceso-denegado")
    public String accesoDenegado(final ModelMap model) {
        return "acceso-denegado";
    }

    @RequestMapping("/informacion/sucursales")
    public String sucursales() {
        return "/informacion/sucursales";
    }
    
    @RequestMapping("/informacion/mapa-google")
    public String mapaGoolge() {
        return "/informacion/mapa-google";
    }
    
    @RequestMapping("/iconos")
    public String icnonos() {
        return "iconos";
    }

}
