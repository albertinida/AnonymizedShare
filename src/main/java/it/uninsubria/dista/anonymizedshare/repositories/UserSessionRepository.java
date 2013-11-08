package it.uninsubria.dista.anonymizedshare.repositories;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.models.UserSession;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = UserSession.class)
public interface UserSessionRepository {
	
	public UserSession findBySocialUser(SocialUser user);
	
	public UserSession findBySessionId(String sessionId);
}
