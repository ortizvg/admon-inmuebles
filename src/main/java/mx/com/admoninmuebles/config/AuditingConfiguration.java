package mx.com.admoninmuebles.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import mx.com.admoninmuebles.persistence.model.Usuario;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditingConfiguration {

    @Bean
    AuditorAware<Usuario> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }

}