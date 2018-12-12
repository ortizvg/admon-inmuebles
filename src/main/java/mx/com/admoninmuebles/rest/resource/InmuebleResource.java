package mx.com.admoninmuebles.rest.resource;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.admoninmuebles.dto.InmuebleDto;
import mx.com.admoninmuebles.service.InmuebleService;

@RestController
@RequestMapping("/api")
public class InmuebleResource {

    @Autowired
    private InmuebleService inmuebleService;

    @GetMapping("/inmuebles")
    public ResponseEntity<Collection<InmuebleDto>> buscarPorColonia(@RequestParam("coloniaId") final Long coloniaId) {
        try {
            Collection<InmuebleDto> inmuebles = inmuebleService.findByDireccionAsentamientoId(coloniaId);
            return new ResponseEntity<>(inmuebles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

    }

}
