package mx.com.admoninmuebles.listener.event;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import mx.com.admoninmuebles.dto.UsuarioDto;

public class OnRegistroCompletoEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;
	
	
    private final String appUrl;
    private final Locale locale;
    private final UsuarioDto usuarioDto;

    public OnRegistroCompletoEvent(final UsuarioDto usuarioDto, final Locale locale, final String appUrl) {
        super(usuarioDto);
        this.usuarioDto = usuarioDto;
        this.locale = locale;
        this.appUrl = appUrl;
    }

	public String getAppUrl() {
		return appUrl;
	}

	public Locale getLocale() {
		return locale;
	}

	public UsuarioDto getUsuarioDto() {
		return usuarioDto;
	}
    
    


}
