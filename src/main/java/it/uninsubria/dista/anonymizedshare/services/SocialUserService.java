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
import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.models.UserSession;
import it.uninsubria.dista.anonymizedshare.repositories.SocialUserRepository;
import it.uninsubria.dista.anonymizedshare.repositories.UserSessionRepository;

@Service
@Transactional
public class SocialUserService {

	@Autowired
	SocialUserRepository socialUserRepository;
	
	@Autowired
	UserSessionRepository userSessionRepository;
	
	public SocialUser create(SocialUser user) throws NullParameterException, NullCreationException, LoginNotValidException {
		
		if (user == null) throw new NullCreationException();
		SocialUser storedUser = socialUserRepository.findByEmail(user.getEmail());

		if (storedUser == null) {
			user = this.createAction(user);
		} else {
			if (storedUser.getName().equals(user.getName()) && storedUser.getSurname().equals(user.getSurname()) && (storedUser.getPassword().equals(user.getPassword()))){
				login(user);
			} else {
				throw new LoginNotValidException();
			}
		}
		return user;
	}

	
	private SocialUser createAction(SocialUser user) throws NullParameterException {

		if (user.getName() != null && user.getSurname() != null && user.getEmail() != null && user.getPassword() != null) {
			
			SocialUser newUser = new SocialUser();
			
			newUser.setUid(new BigInteger(64, new SecureRandom()));
			newUser.setName(user.getName());
			newUser.setSurname(user.getSurname());
			newUser.setEmail(user.getEmail());
			newUser.setPassword(user.getPassword());			
			newUser.setSecret(new BigInteger(64, new SecureRandom()).toString(16));
			
			socialUserRepository.saveAndFlush(newUser);

			return newUser;
			
		} else {
			throw new NullParameterException();
		}
	}

	
	public SocialUser login(SocialUser user) throws LoginNotValidException, NullParameterException {
		
		if (user == null) throw new NullParameterException();
		
		SocialUser storedUser = socialUserRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
		if (storedUser != null) {
			
			UserSession newSession = new UserSession();
			newSession.setUser(storedUser);
			newSession.setSessionId(new BigInteger(64, new SecureRandom()).toString(16));
			
			userSessionRepository.saveAndFlush(newSession);
			
			return storedUser;
		} else {
			throw new LoginNotValidException();
		}
	}


	
	public boolean deleteUser(String email) {
		// TODO Auto-generated method stub
		SocialUser user = socialUserRepository.findByEmail(email);
		if (user!=null) {
			this.deleteAction(user);
			return true;
		}else
			return false;
	}

	private void deleteAction(SocialUser user) {
		socialUserRepository.delete(user);			
	}
	
	
}
