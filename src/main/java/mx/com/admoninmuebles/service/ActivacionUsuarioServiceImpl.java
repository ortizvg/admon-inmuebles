package mx.com.admoninmuebles.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.dto.ActivacionUsuarioDto;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.model.ActivacionUsuarioToken;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;
import mx.com.admoninmuebles.persistence.repository.ActivacionUsuarioTokenRepository;

@Service
public class ActivacionUsuarioServiceImpl implements ActivacionUsuarioService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ActivacionUsuarioTokenRepository activacionUsuarioTokenRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UsuarioDto activar(ActivacionUsuarioDto activacionUsuarioDto) {
		Optional<ActivacionUsuarioToken> activacionUsuarioTokenOpt = activacionUsuarioTokenRepository.findByToken(activacionUsuarioDto.getToken());
		ActivacionUsuarioToken activacionUsuarioToken = activacionUsuarioTokenOpt.get();
		
		Usuario usuario = activacionUsuarioToken.getUsuario();
		
		usuario.setContrasenia(passwordEncoder.encode(activacionUsuarioDto.getContrasenia()));
		usuario.setActivo(true);
		
		Usuario usuarioModificado = usuarioRepository.save(usuario);
		
		activacionUsuarioTokenRepository.deleteByToken(activacionUsuarioToken.getToken());
		
		return modelMapper.map(usuarioModificado, UsuarioDto.class);
	}
	
	
    @Override
    public void guardarToken(final UsuarioDto usuarioDto, final String token) {
    	Usuario usuario = usuarioRepository.findById(usuarioDto.getId()).get();
    	Optional<ActivacionUsuarioToken> activacionUsuarioTokenOpt = activacionUsuarioTokenRepository.findByUsuarioId(usuario.getId());
    	if(activacionUsuarioTokenOpt.isPresent()) {
    		activacionUsuarioTokenRepository.deleteByToken(activacionUsuarioTokenOpt.get().getToken());
    	}
        final ActivacionUsuarioToken verificacionToken = new ActivacionUsuarioToken(token, usuario);
        activacionUsuarioTokenRepository.save(verificacionToken);
    }
    
    @Override
    public boolean isTokenValido(final String token) {
    	Optional<ActivacionUsuarioToken> verificacionTokenOpt = activacionUsuarioTokenRepository.findByToken(token);
    	if(!verificacionTokenOpt.isPresent()) {
    		return false;
    	}
    	
    	return !verificacionTokenOpt.get().isUtilizado();
    }
	
	

}
