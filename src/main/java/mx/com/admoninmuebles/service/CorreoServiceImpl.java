package mx.com.admoninmuebles.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;

import mx.com.admoninmuebles.dto.CorreoDto;
import mx.com.admoninmuebles.error.BusinessException;

@Service
public class CorreoServiceImpl implements CorreoService{
	
	@Autowired
    private JavaMailSender sender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Override
	public void enviarCorreo(CorreoDto correoDto) {
		MimeMessage message = sender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	    
	    try {
	    	String plantilla = templateEngine.process(correoDto.getPlantilla(), correoDto.getDatosPlantilla());
	    	helper.setFrom(correoDto.getDe());
	        helper.setTo(correoDto.getPara());
	        helper.setSubject(correoDto.getAsunto());
	        helper.setText(plantilla, true);
	        if(correoDto.getConCopiaPara() != null && !correoDto.getConCopiaPara().isEmpty()) {
	        	helper.setCc(correoDto.getConCopiaPara());
	        }
	        sender.send(message);
	    } catch (MessagingException e) {
	        throw new BusinessException(e.getMessage(), e);
	    } catch (Exception e) {
	    	throw new BusinessException(e.getMessage(), e);
	    }
	}

}
