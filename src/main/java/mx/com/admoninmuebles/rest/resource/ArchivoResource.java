package mx.com.admoninmuebles.rest.resource;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mx.com.admoninmuebles.dto.ArchivoDto;
import mx.com.admoninmuebles.error.BusinessException;
import mx.com.admoninmuebles.persistence.model.Archivo;
import mx.com.admoninmuebles.service.ArchivoService;

@RestController
@RequestMapping("/api")
public class ArchivoResource {
	
    @Autowired
    private ArchivoService archivoService;
    
    @Autowired
    private ServletContext servletContext;
    
    @Autowired
    private MessageSource messages;
    
    @GetMapping(value = "/archivos/{id}")
    public ResponseEntity<ByteArrayResource> obtenerArchivo(@PathVariable String id) {
    	
    	ArchivoDto archivo = archivoService.buscarPorId(id);
 	    String strMediaType = servletContext.getMimeType(archivo.getNombre());
 	    MediaType mediaType = MediaType.parseMediaType(strMediaType);
 	    ByteArrayResource resource = new ByteArrayResource(archivo.getBytes());
 	    return ResponseEntity.ok()
                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+archivo.getNombre())
                 .contentType(mediaType) //
                 .contentLength(archivo.getBytes().length) 
                 .body(resource);
    }
    
    
    @GetMapping(value = "/archivos")
    public ResponseEntity<List<String>> obtenerArchivo() {
        
    	Collection<ArchivoDto> archivos = archivoService.buscarTodo();
    	
    	List<String> nombres = archivos.stream()
				.map(archivo -> archivo.getId().toString())
				.collect(Collectors.toList());
    	
    	return ResponseEntity.ok().body(nombres);
    }
    
    @PostMapping("/archivos/carga")
    public  ResponseEntity<String> guardarArchivo(@RequestParam("archivo") MultipartFile archivo, Locale locale) {
    	
    	if(  archivo == null ) {
    		return new ResponseEntity<String>(messages.getMessage("archivo.error.carga.vacio", null, locale), HttpStatus.BAD_REQUEST);
    	}
    	
		try {
			
			ArchivoDto archivoDto = new ArchivoDto();
			archivoDto.setBytes(archivo.getBytes());
			archivoDto.setNombre(archivo.getOriginalFilename());
			archivoDto.setTipoContenido(archivo.getContentType());
			archivoService.guardar(archivoDto);
			
			return ResponseEntity.ok().body("OK");
		} catch (IOException e) {
			return new ResponseEntity<String>(messages.getMessage("archivo.error.carga", null, locale), HttpStatus.BAD_REQUEST);
		}
    }
    
}
