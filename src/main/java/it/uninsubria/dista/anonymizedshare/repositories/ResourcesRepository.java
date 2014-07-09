package it.uninsubria.dista.anonymizedshare.repositories;

import java.math.BigInteger;
import java.util.Set;

import it.uninsubria.dista.anonymizedshare.models.Resource;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Resource.class)
public interface ResourcesRepository {
	
	public Resource findByName(String name);
	
	public Resource findById(long id);
	
	public Resource findByNameAndUserOwner(String name,SocialUser userOwner);
	
	public Set<Resource> findByUserOwner(SocialUser userOwner);
	
	public void delete(Resource resource);
}
