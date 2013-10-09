package it.uninsubria.dista.anonymizedshare.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uninsubria.dista.anonymizedshare.exceptions.CreationParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.models.Resource;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.repositories.ResourcesRepository;
import it.uninsubria.dista.anonymizedshare.services.interfaces.ResourceServiceInterface;

@Service
@Transactional
public class ResourceService implements ResourceServiceInterface {

	@Autowired
	ResourcesRepository resourcesRepository;
	
	
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
