
package mx.com.admoninmuebles.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@ComponentScan
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    public void configAuthentication(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordencoder());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/").permitAll().and().authorizeRequests().antMatchers("/console/**").permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();
        // @formatter:off
        http.authorizeRequests().requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().and()
            .authorizeRequests().antMatchers("/assets/**", "/files/**").permitAll().and()
            .authorizeRequests()
            .antMatchers("/inicio*", "/login*", "/contacto*", "/iconos*", "/preguntas-frecuentes*").permitAll()
            .antMatchers("/informacion/**").permitAll()
            .antMatchers("/api/**").permitAll()
            .antMatchers("/actuator/**").permitAll()
            .antMatchers("/usuarios/activar/**").permitAll()
            .antMatchers("/usuarios/correo-recuperar-contrasenia/**").permitAll()
            .antMatchers("/usuarios/recuperar-contrasenia-peticion/**").permitAll()
            .antMatchers("/usuarios/recuperar-contrasenia/**").permitAll()
            .antMatchers("/api/**").permitAll()
            .antMatchers("/proveedores/inicio").hasRole("PROVEEDOR")
            .antMatchers("/sociobi/inicio").hasRole("SOCIO_BI")
                .antMatchers("/invalidSession*").anonymous()
                .antMatchers("/catalogos/**").authenticated()
                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedPage("/acceso-denegado")
//                .and().exceptionHandling().accessDeniedPage("/error/403")
                .and()
            .formLogin()
                .loginPage("/login")
                .successHandler(customUrlAuthenticationSuccessHandler())
//                .defaultSuccessUrl("/user/index")
                .failureUrl("/login?error=true")
                .failureHandler(authenticationFailureHandler)
            .permitAll()
                .and()
            .sessionManagement()
                .invalidSessionUrl("/")
                .sessionFixation().none()
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .permitAll()
            .and()
                .rememberMe().key("uniqueAndSecret");
    // @formatter:on
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordencoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationSuccessHandler customUrlAuthenticationSuccessHandler(){
        return new CustomUrlAuthenticationSuccessHandler();
    }

}
