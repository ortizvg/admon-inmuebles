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
import mx.com.admoninmuebles.listener.event.OnRegistroCompletoEvent;
import mx.com.admoninmuebles.service.ActivacionUsuarioService;

@Component
public class RegistroUsuarioListener implements ApplicationListener<OnRegistroCompletoEvent> {
	
	@Autowired
    private ActivacionUsuarioService activacionUsuarioService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

	@Override
	public void onApplicationEvent(OnRegistroCompletoEvent event) {
		confirmarRegistro(event);
	}
	
	private void confirmarRegistro(final OnRegistroCompletoEvent event) {
        final UsuarioDto usuarioDto = event.getUsuarioDto();
        final String token = UUID.randomUUID().toString();
        activacionUsuarioService.guardarToken(usuarioDto, token);

        final SimpleMailMessage email = crearCorreoActivacion(event, usuarioDto, token);
        mailSender.send(email);
    }

    //

    private final SimpleMailMessage crearCorreoActivacion(final OnRegistroCompletoEvent event, final UsuarioDto usuarioDto, final String token) {
        final String correo = usuarioDto.getCorreo();
        final String subject = "Activación de cuenta";
        final String activacionUrl = event.getAppUrl() + "/usuarios/activar/" + token;
//        final String message = messages.getMessage("message.regSucc", null, event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(correo);
        email.setSubject(subject);
        email.setText("Clic en el link para activar y confirmar la cuenta" + " \r\n" + activacionUrl);
//        email.setFrom(env.getProperty("support.email"));
        email.setFrom("pass.usuarios@gesco-pls.com");
        return email;
    }




}
