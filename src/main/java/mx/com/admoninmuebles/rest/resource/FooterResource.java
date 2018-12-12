package mx.com.admoninmuebles.rest.resource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import mx.com.admoninmuebles.constant.LocaleConst;


@RestController
@RequestMapping("/api")
public class FooterResource {

	@GetMapping("/aviso-privacidad")
	public @ResponseBody HttpEntity<byte[]> descarAvisoPrivacidad(HttpServletRequest request, final Locale locale) {
		try {
			String avisoPrivacidadNombre = "gesco/AVISO_GESCO_ESP.pdf";
			if(LocaleConst.LOCALE_EN.equalsIgnoreCase(locale.getLanguage())) {
				avisoPrivacidadNombre = "gesco/AVISO_GESCO_ENG.pdf";
			}
			
			InputStream avisoPrivacidadIs = Thread.currentThread().getContextClassLoader().getResourceAsStream(avisoPrivacidadNombre);
			
			if(avisoPrivacidadIs == null) {
				throw new FileNotFoundException();
			}
			
			byte[] document = IOUtils.toByteArray(avisoPrivacidadIs);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "pdf"));
			header.set("Content-Disposition", "inline; filename=" + avisoPrivacidadNombre);
			header.setContentLength(document.length);
			return new HttpEntity<byte[]>(document, header);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

	}

}
