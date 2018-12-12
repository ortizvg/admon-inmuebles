package mx.com.admoninmuebles.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.listener.event.OnRecuperacionContraseniaEvent;
import mx.com.admoninmuebles.persistence.model.RecuperacionContraseniaToken;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.repository.RecuperacionContraseniaTokenRepository;
import mx.com.admoninmuebles.service.RecuperacionContraseniaService;

@Component
public class RecuperacionContraseniaListener implements ApplicationListener<OnRecuperacionContraseniaEvent>{

	
	@Autowired
    private RecuperacionContraseniaService recuperacionContraseniaService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;
	
	@Override
	public void onApplicationEvent(OnRecuperacionContraseniaEvent event) {
		recuperarContrasenia(event);
	}
	
	private void recuperarContrasenia(final OnRecuperacionContraseniaEvent event) {
        final UsuarioDto usuarioDto = event.getUsuarioDto();
        final String token = UUID.randomUUID().toString();
        
        recuperacionContraseniaService.guardarToken(usuarioDto, token);
        
        final SimpleMailMessage email = crearCorreoRecuperacion(event, usuarioDto, token);
        mailSender.send(email);
        System.out.println("Correo enviado a " + usuarioDto.getCorreo());
	}

    //

    private final SimpleMailMessage crearCorreoRecuperacion(final OnRecuperacionContraseniaEvent event, final UsuarioDto usuarioDto, final String token) {
        final String correo = usuarioDto.getCorreo();
        final String subject = "Recuperacion de contraseña";
        final String confirmacionnUrl = event.getAppUrl() + "/usuarios/recuperar-contrasenia/" + token;
//        final String message = messages.getMessage("message.regSucc", null, event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(correo);
        email.setSubject(subject);
        email.setText("Clic en el link para recuperar tu contraseña" + " \r\n" + confirmacionnUrl);
//        email.setFrom(env.getProperty("support.email"));
        email.setFrom("prueba@gesco-pls.com");
        return email;
    }

}
