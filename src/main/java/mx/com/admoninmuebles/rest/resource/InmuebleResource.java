package mx.com.admoninmuebles.rest.resource;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.admoninmuebles.dto.AreaComunDto;
import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.service.InmuebleService;

@RestController
@RequestMapping("/api")
public class InmuebleResource {

    @Autowired
    private InmuebleService inmuebleService;

    @GetMapping("/inmuebles")
    public ResponseEntity<Collection<InmuebleDto>> buscarPorColonia(@RequestParam(name = "coloniaId", required = false) final Long coloniaId, @RequestParam(name = "zonaCodigo", required = false) final String zonaCodigo) {
        try {
        	Collection<InmuebleDto> inmuebles =  Collections.emptyList();
        	if(!StringUtils.isEmpty(zonaCodigo)) {
        		inmuebles = inmuebleService.findByZonaCodigo(zonaCodigo);
        	} else if( coloniaId != null ) {
        		inmuebles = inmuebleService.findByDireccionAsentamientoId(coloniaId);
        	}
            return new ResponseEntity<>(inmuebles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

    }
    
    @GetMapping("/inmuebles/{id}/socios")
    public ResponseEntity<Collection<UsuarioDto>> buscarSociosPorInmueble( @PathVariable Long id ) {
        try {
        	Collection<UsuarioDto> socios =  inmuebleService.findSociosByInmuebleId( id );
            return new ResponseEntity<>(socios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }
    
    @GetMapping("/inmuebles/{id}/areas-comunes")
    public ResponseEntity<Collection<AreaComunDto>> buscarAreasComunesPorInmueble( @PathVariable Long id ) {
        try {
        	InmuebleDto inmueble =  inmuebleService.findById( id );
            return new ResponseEntity<>(inmueble.getAreasComunes(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

    }

}
