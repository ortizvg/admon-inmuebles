package mx.com.admoninmuebles.rest.resource;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CargaMasivaResource {
	
	@GetMapping("/carga-masiva/manual")
	public @ResponseBody HttpEntity<byte[]> descarManualCargaMasiva(HttpServletRequest request, final Locale locale) {
		try {
			String manualCargaMasiva = "gesco/MANUAL_CARGA_MASIVA.docx";
//			if(LocaleConst.LOCALE_EN.equalsIgnoreCase(locale.getLanguage())) {
//				avisoPrivacidadNombre = "gesco/AVISO_GESCO_ENG.pdf";
//			}
			
			InputStream avisoPrivacidadIs = Thread.currentThread().getContextClassLoader().getResourceAsStream(manualCargaMasiva);
			
			if(avisoPrivacidadIs == null) {
				throw new FileNotFoundException();
			}
			
			byte[] document = IOUtils.toByteArray(avisoPrivacidadIs);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "docx"));
			header.set("Content-Disposition", "attachment; filename=" + manualCargaMasiva);
			header.setContentLength(document.length);
			return new HttpEntity<byte[]>(document, header);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

	}
	
	@GetMapping("/carga-masiva/plantilla")
	public @ResponseBody HttpEntity<byte[]> descarAvisoPrivacidad(HttpServletRequest request, final Locale locale) {
		try {
			String plantillaCargaMasiva = "gesco/PLANTILLA_CARGA_MASIVA.csv";
//			if(LocaleConst.LOCALE_EN.equalsIgnoreCase(locale.getLanguage())) {
//				avisoPrivacidadNombre = "gesco/AVISO_GESCO_ENG.pdf";
//			}
			
			InputStream avisoPrivacidadIs = Thread.currentThread().getContextClassLoader().getResourceAsStream(plantillaCargaMasiva);
			
			if(avisoPrivacidadIs == null) {
				throw new FileNotFoundException();
			}
			
			byte[] document = IOUtils.toByteArray(avisoPrivacidadIs);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "text"));
			header.set("Content-Disposition", "attachment; filename=" + plantillaCargaMasiva);
			header.setContentLength(document.length);
			return new HttpEntity<byte[]>(document, header);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

	}

}
