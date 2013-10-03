package it.uninsubria.dista.anonymizedshare.repositories;

import java.util.Date;
import java.util.Set;

import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = SocialUser.class)
public interface SocialUserRepository {
	
	public SocialUser findByEmail(String email);
	
	public SocialUser findByNameAndSurnameAndBirthday(String name, String surname, Date birthday);
	
}
