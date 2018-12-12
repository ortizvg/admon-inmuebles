package mx.com.admoninmuebles.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mx.com.admoninmuebles.dto.RecuperaContraseniaDto;
import mx.com.admoninmuebles.dto.UsuarioDto;
import mx.com.admoninmuebles.persistence.model.RecuperacionContraseniaToken;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.repository.RecuperacionContraseniaTokenRepository;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;

@Service
public class RecuperacionContraseniaServiceImpl implements RecuperacionContraseniaService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RecuperacionContraseniaTokenRepository recuperacionContraseniaTokenRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UsuarioDto guardarNuevaContrasenia(RecuperaContraseniaDto recuperaContraseniaDto) {
		Optional<RecuperacionContraseniaToken> recuperacionContraseniaTokenOpt = recuperacionContraseniaTokenRepository.findByToken(recuperaContraseniaDto.getToken());
		RecuperacionContraseniaToken recuperacionContraseniaToken = recuperacionContraseniaTokenOpt.get();
		
		Usuario usuario = recuperacionContraseniaToken.getUsuario();
		
		usuario.setContrasenia(passwordEncoder.encode(recuperaContraseniaDto.getContrasenia()));
		
		Usuario usuarioModificado = usuarioRepository.save(usuario);
		
		recuperacionContraseniaTokenRepository.deleteByToken(recuperacionContraseniaToken.getToken());
		
		return modelMapper.map(usuarioModificado, UsuarioDto.class);
	}

	@Override
	public void guardarToken(UsuarioDto usuarioDto, String token) {
		Usuario usuario = usuarioRepository.findById(usuarioDto.getId()).get();
		RecuperacionContraseniaToken recuperacionContraseniaToken = usuario.getRecuperacionContraseniaToken();
		if(recuperacionContraseniaToken != null) {
			recuperacionContraseniaTokenRepository.deleteByToken(recuperacionContraseniaToken.getToken());
		}
		RecuperacionContraseniaToken recuperacionContraseniaTokenNuevo = new RecuperacionContraseniaToken(token, usuario);
        recuperacionContraseniaTokenRepository.save(recuperacionContraseniaTokenNuevo);
	}
	
    @Override
    public boolean isTokenValido(final String token) {
    	Optional<RecuperacionContraseniaToken> recuperacionContraseniaToken = recuperacionContraseniaTokenRepository.findByToken(token);
    	if(!recuperacionContraseniaToken.isPresent()) {
    		return false;
    	}
    	
    	return !recuperacionContraseniaToken.get().isUtilizado();
    }

}
