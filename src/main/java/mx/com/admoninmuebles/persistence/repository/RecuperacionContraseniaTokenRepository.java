package mx.com.admoninmuebles.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.persistence.model.RecuperacionContraseniaToken;

public interface RecuperacionContraseniaTokenRepository extends JpaRepository<RecuperacionContraseniaToken, Long>{
	
	Optional<RecuperacionContraseniaToken> findByToken(String token);
	
	@Modifying
    @Transactional
    @Query("delete from RecuperacionContraseniaToken u where u.token = ?1")
	void deleteByToken(String token);
}
