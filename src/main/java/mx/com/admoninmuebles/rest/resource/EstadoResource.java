package mx.com.admoninmuebles.rest.resource;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.admoninmuebles.dto.EstadoDto;
import mx.com.admoninmuebles.service.EstadoService;

@RestController
@RequestMapping("/api")
public class EstadoResource {

	@Autowired
	private EstadoService estadoService;

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/estados")
	public ResponseEntity<Collection<EstadoDto>> buscarEstados() {
		
		try {
			Collection<EstadoDto> estados = estadoService.buscarTodo();
			return new ResponseEntity<>(estados, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
		}

	}

}
