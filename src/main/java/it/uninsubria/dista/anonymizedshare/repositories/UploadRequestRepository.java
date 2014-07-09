package it.uninsubria.dista.anonymizedshare.repositories;

import java.math.BigInteger;
import java.util.Set;

import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.models.UploadRequest;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;


@RooJpaRepository(domainType = UploadRequest.class)
public interface UploadRequestRepository {
	
	public UploadRequest findById(BigInteger id);
	
	public Set<UploadRequest> findBySocialUser(SocialUser socialUser);
}
