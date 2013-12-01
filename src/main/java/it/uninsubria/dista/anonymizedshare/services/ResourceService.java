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
	
	public Resource getResource(long id) {
		Resource resource = resourcesRepository.findById(id);
		return resource;
	}
	
	public void setModificationDate(Resource resource, Date date) {
		Resource temp = resourcesRepository.findById(resource.getId());
		temp.setLastModification(new Date());
		resourcesRepository.saveAndFlush(temp);
	}
	
	public void setSharingDepth(Resource resource, int sharingDepth) {
		Resource temp = resourcesRepository.findById(resource.getId());
		temp.setSharingDepth(sharingDepth);
		resourcesRepository.saveAndFlush(temp);
	}
	
	public Resource create(Resource resource) throws CreationParameterNotValidException {
		
		Resource storedResource = resourcesRepository.findById(resource.getId());
		
		if(storedResource == null)			
			storedResource = this.createAction(resource);
			
		return storedResource;
	}

	
	public boolean deleteResource(String name) {
		Resource resource = resourcesRepository.findByName(name);
		if(resource != null) {
			this.deleteAction(name);
			return true;
		}else
			return false;
	}
	
	private Resource createAction(Resource resource) throws CreationParameterNotValidException {
		if(resource.getUserOwner() != null && resource.getName() != null && resource.getMimeType() != null && resource.getSize() >= 0) {
			Resource newResource = new Resource();
			newResource.setId(resource.getId());
			newResource.setUserOwner(resource.getUserOwner());
			newResource.setName(resource.getName());
			newResource.setMimeType(resource.getMimeType());
			newResource.setSize(resource.getSize());
			resourcesRepository.saveAndFlush(newResource);
			return newResource;
		}else
			throw new CreationParameterNotValidException();		
	}
	
	private void deleteAction(String name) {
		Resource resource = resourcesRepository.findByName(name);
		resourcesRepository.delete(resource);
	}

}
