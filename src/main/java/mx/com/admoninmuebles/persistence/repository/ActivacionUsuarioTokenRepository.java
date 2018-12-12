package mx.com.admoninmuebles.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.persistence.model.ActivacionUsuarioToken;

public interface ActivacionUsuarioTokenRepository extends CrudRepository<ActivacionUsuarioToken, Long>{
	
	Optional<ActivacionUsuarioToken> findByToken(String token);
	
	Optional<ActivacionUsuarioToken> findByUsuarioId(Long id);
	
	ActivacionUsuarioToken saveAndFlush(ActivacionUsuarioToken activacionUsuarioToken);
	
	@Modifying
	@Transactional
	@Query("delete from ActivacionUsuarioToken u where u.token = ?1")
	void deleteByToken(String token);
}
