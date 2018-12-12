package mx.com.admoninmuebles.rest.resource;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.admoninmuebles.dto.ColoniaDto;
import mx.com.admoninmuebles.service.ColoniaService;

@RestController
@RequestMapping("/api")
public class ColoniaResource {
	
    @Autowired
    private ColoniaService coloniaService;
    
	@GetMapping("/colonias")
	public ResponseEntity<Collection<ColoniaDto>> buscarColonias(@RequestParam(name = "codigoPostal", required = false) String codigoPostal , @RequestParam(name = "zonaCodigo", required = false) String zonaCodigo) {
		try {
			Collection<ColoniaDto> colonias =  Collections.emptyList();
			if( (!StringUtils.isEmpty(codigoPostal) && !StringUtils.isEmpty(zonaCodigo))) {
				colonias = coloniaService.findBycodigoPostalAndZonaCodigo(codigoPostal, zonaCodigo);
			} else if(!StringUtils.isEmpty(codigoPostal) && StringUtils.isEmpty(zonaCodigo)){
				colonias = coloniaService.findBycodigoPostal(codigoPostal);
			} else if(StringUtils.isEmpty(codigoPostal) && !StringUtils.isEmpty(zonaCodigo)){
				colonias = coloniaService.findByZonaCodigo(zonaCodigo);
			}
			return new ResponseEntity<>(colonias, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		}
		
	}
	
}
