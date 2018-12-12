package mx.com.admoninmuebles.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.security.CustomUserDetails;

public class SpringSecurityAuditorAware implements AuditorAware<Usuario> {

    @Override
    public Optional<Usuario> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof CustomUserDetails) {
                return ((CustomUserDetails) authentication.getPrincipal()).getUsuario();
            }
            return null;
        });

    }
}
