package it.uninsubria.dista.anonymizedshare.repositories;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = SocialUser.class)
public interface SocialUserRepository {
	
	public SocialUser findByUid(BigInteger uid);
	
	public SocialUser findByEmail(String email);
	
	public SocialUser findByEmailAndPassword(String email,String password);
		
}
