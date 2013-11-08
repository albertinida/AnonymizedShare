package it.uninsubria.dista.anonymizedshare.repositories;
import it.uninsubria.dista.anonymizedshare.models.UserSession;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = UserSession.class)
public interface UserSessionRepository {
}
