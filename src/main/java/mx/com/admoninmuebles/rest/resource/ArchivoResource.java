package mx.com.admoninmuebles.rest.resource;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mx.com.admoninmuebles.dto.ArchivoDto;
import mx.com.admoninmuebles.dto.TicketDto;
import mx.com.admoninmuebles.service.ArchivoService;

@RestController
@RequestMapping("/api")
public class ArchivoResource {
	
    @Autowired
    private ArchivoService archivoService;
    
    @Autowired
    private ServletContext servletContext;
    
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
    
    
//    @GetMapping(value = "/archivos/{id}")
//    public @ResponseBody byte[] obtenerArchivo(@PathVariable String id) {
//    	
////    public @ResponseBody byte[] obtenerArchivo(@PathVariable String id, @RequestParam(name = "formato", required = false) String formato) {
//    	
//    	
////        InputStream in = servletContext.getResourceAsStream("/WEB-INF/images/image-example.jpg");
//        
//    	ArchivoDto archivo = archivoService.buscarPorId(id);
//    	
//    	return archivo.getBytes();
//    }
    
    @GetMapping(value = "/archivos")
    public ResponseEntity<List<String>> obtenerArchivo() {
        
    	Collection<ArchivoDto> archivos = archivoService.buscarTodo();
    	
    	List<String> nombres = archivos.stream()
				.map(archivo -> archivo.getId().toString())
				.collect(Collectors.toList());
    	
    	return ResponseEntity.ok().body(nombres);
    }
    
}
