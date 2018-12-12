package mx.com.admoninmuebles.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.persistence.model.Rol;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        Optional<Usuario> optUsuario = userRepository.findByUsername(username);
        if (optUsuario.isPresent()) {
            Usuario usuario = optUsuario.get();
            usuario.getRoles();
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (Rol rol : usuario.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(rol.getNombre()));
                rol.getPrivilegios().stream().map(p -> new SimpleGrantedAuthority(p.getNombre())).forEach(authorities::add);
            }
            return new CustomUserDetails(usuario, authorities);
        } else {
            throw new UsernameNotFoundException("No user present with username: " + username);
        }
    }

}
