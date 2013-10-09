package it.uninsubria.dista.anonymizedshare.repositories;

import it.uninsubria.dista.anonymizedshare.models.Resource;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Resource.class)
public interface ResourcesRepository {
	
	public Resource findByName(String name);
	
	public Resource findByNameAndUserOwner(String name,SocialUser userOwner);
	
	public void delete(Resource resource);
}
