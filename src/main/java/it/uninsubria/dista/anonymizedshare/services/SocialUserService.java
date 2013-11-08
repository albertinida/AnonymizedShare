package it.uninsubria.dista.anonymizedshare.services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uninsubria.dista.anonymizedshare.exceptions.CreationParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.exceptions.LoginParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.repositories.SocialUserRepository;
import it.uninsubria.dista.anonymizedshare.services.interfaces.SocialUserServiceInterface;

@Service
@Transactional

public class SocialUserService implements SocialUserServiceInterface {

	@Autowired
	SocialUserRepository socialUserRepository;
	
	public SocialUser create(String name, String surname, Calendar birthday, String email) throws CreationParameterNotValidException {
		
		SocialUser user = socialUserRepository.findByEmail(email);

		if (user == null) {
			
			user = this.createAction(name, surname, birthday, email);
			
		}
		
		return user;
	}
	
	public SocialUser login(String email, String password) throws LoginParameterNotValidException {
		SocialUser user = socialUserRepository.findByEmailAndPassword(email, password);
		if(user != null)
			return user;
		else
			throw new LoginParameterNotValidException();
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

	private SocialUser createAction(String name, String surname, Calendar birthday, String email) throws CreationParameterNotValidException {

		if (name != null && surname != null && birthday != null && email != null) {
			
			SocialUser user = new SocialUser();
			user.setName(name);
			user.setSurname(surname);
			user.setBirthday(birthday);
			user.setEmail(email);
			
			user.setUserSecret(new BigInteger(130, new SecureRandom()).toString(32));
			
			socialUserRepository.saveAndFlush(user);

			return user;
			
		} else {
			throw new CreationParameterNotValidException();
		}
		
		
	}

	private void deleteAction(SocialUser user) {
		socialUserRepository.delete(user);			
	}
	
	
}
