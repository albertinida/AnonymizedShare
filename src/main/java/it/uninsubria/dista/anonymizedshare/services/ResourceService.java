package it.uninsubria.dista.anonymizedshare.services;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uninsubria.dista.anonymizedshare.exceptions.CreationParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.models.Resource;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.repositories.ResourcesRepository;
import it.uninsubria.dista.anonymizedshare.repositories.SocialUserRepository;

@Service
@Transactional
public class ResourceService {

	@Autowired
	SocialUserRepository socialUserRepository;
	@Autowired
	ResourcesRepository resourcesRepository;
	
	public boolean exists(long resourceId, BigInteger userId) {
		SocialUser user = socialUserRepository.findByUid(userId);
		Set<Resource> resources = resourcesRepository.findByUserOwner(user);
		if(resources.isEmpty())
			return false;
		else 
			return true;
	}
	
	public void setModificationDate(Resource resource, Date date) {
		Resource temp = resourcesRepository.findById(resource.getId());
		temp.setLastModification(new Date());
		resourcesRepository.saveAndFlush(temp);
	}
	
	public Resource create(SocialUser userOwner, String name, String mimeType,
			long size) throws CreationParameterNotValidException {
		
		Resource resource = resourcesRepository.findByNameAndUserOwner(name, userOwner);
		
		if(resource == null) {
			
			resource = this.createAction(userOwner, name, mimeType, size);
			
		}
		return resource;
	}

	
	public boolean deleteResource(String name) {
		Resource resource = resourcesRepository.findByName(name);
		if(resource != null) {
			this.deleteAction(name);
			return true;
		}else
			return false;
	}
	
	private Resource createAction(SocialUser userOwner,String name,String mimeType,long size) throws CreationParameterNotValidException {
		if(userOwner != null && name != null && mimeType != null && size >= 0) {
			Resource resource = new Resource();
			resource.setUserOwner(userOwner);
			resource.setName(name);
			resource.setMimeType(mimeType);
			resource.setSize(size);
			resource.setSharingDepth(3);
			resourcesRepository.saveAndFlush(resource);
			return resource;
		}else
			throw new CreationParameterNotValidException();		
	}
	
	private void deleteAction(String name) {
		Resource resource = resourcesRepository.findByName(name);
		resourcesRepository.delete(resource);
	}

}
