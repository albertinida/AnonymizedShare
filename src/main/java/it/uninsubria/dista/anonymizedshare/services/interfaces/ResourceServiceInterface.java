package it.uninsubria.dista.anonymizedshare.services.interfaces;

import it.uninsubria.dista.anonymizedshare.exceptions.CreationParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.models.Resource;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;

public interface ResourceServiceInterface {

	public Resource create(SocialUser userOwner,String name,String mimeType, long size) throws CreationParameterNotValidException;
	
	public boolean deleteResource(String name);
	
}
