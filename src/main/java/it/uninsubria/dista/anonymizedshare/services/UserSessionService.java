package it.uninsubria.dista.anonymizedshare.services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uninsubria.dista.anonymizedshare.exceptions.CreationParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.exceptions.LoginNotValidException;
import it.uninsubria.dista.anonymizedshare.exceptions.LoginParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.exceptions.NullCreationException;
import it.uninsubria.dista.anonymizedshare.exceptions.NullParameterException;
import it.uninsubria.dista.anonymizedshare.exceptions.NullSessionException;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.models.UserSession;
import it.uninsubria.dista.anonymizedshare.repositories.SocialUserRepository;
import it.uninsubria.dista.anonymizedshare.repositories.UserSessionRepository;

@Service
@Transactional
public class UserSessionService {

	@Autowired
	SocialUserRepository socialUserRepository;
	
	@Autowired
	UserSessionRepository userSessionRepository;
	
	public UserSession fetchSession(SocialUser user) throws NullSessionException {
		 
		UserSession activeSession = userSessionRepository.findBySocialUser(user);
		if (activeSession != null)
			return activeSession;
		else
			throw new NullSessionException();
	}
	
	
	public SocialUser fetchUser(String sessionId) throws NullSessionException {
		
		return fetchAction(userSessionRepository.findBySessionId(sessionId));
	}
	
	public SocialUser fetchUser(UserSession session) throws NullSessionException {
		
		return fetchAction(userSessionRepository.findBySessionId(session.getSessionId()));
	}
	
	private SocialUser fetchAction(UserSession storedSession) throws NullSessionException {
		
		if (storedSession != null) {
			return storedSession.getSocialUser();
		} else {
			throw new NullSessionException();
		}

	}
}
