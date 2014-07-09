package it.uninsubria.dista.anonymizedshare.services;

import java.math.BigInteger;

import it.uninsubria.dista.anonymizedshare.exceptions.NullParameterException;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.models.UploadRequest;
import it.uninsubria.dista.anonymizedshare.repositories.SocialUserRepository;
import it.uninsubria.dista.anonymizedshare.repositories.UploadRequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UploadRequestService {
	
	@Autowired
	UploadRequestRepository uploadRequestRepository;
	
	@Autowired
	SocialUserRepository socialUserRepository;
	
	public UploadRequest createNewUploadRequest(UploadRequest uploadRequest) throws NullParameterException{
		
		if(uploadRequest == null) throw new NullParameterException();
		
		UploadRequest newRequest = uploadRequestRepository.findById(uploadRequest.getId());
		if(newRequest == null)
			newRequest = this.createAction(uploadRequest);
		return newRequest;
	}
	
	public UploadRequest getUploadRequest(String token) throws NullParameterException {
		if(token == null)
			throw new NullParameterException();
		UploadRequest request = uploadRequestRepository.findById(new BigInteger(token));
		return request;
	}
	
	private UploadRequest createAction(UploadRequest request) throws NullParameterException{
		if(request.getId()!= null && request.getSocialUser()!=null) {
			UploadRequest uploadRequest = new UploadRequest();
			SocialUser socialUser = socialUserRepository.findByUid(request.getSocialUser().getUid());
			uploadRequest.setId(request.getId());
			uploadRequest.setSocialUser(socialUser);
			uploadRequestRepository.saveAndFlush(uploadRequest);
			return uploadRequest;
		}else
			throw new NullParameterException();
	}

}
