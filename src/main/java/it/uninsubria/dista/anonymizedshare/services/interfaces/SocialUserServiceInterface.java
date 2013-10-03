package it.uninsubria.dista.anonymizedshare.services.interfaces;

import it.uninsubria.dista.anonymizedshare.exceptions.CreationParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;

import java.util.Calendar;
import java.util.Date;

public interface SocialUserServiceInterface {

	public SocialUser create(String name, String surname, Calendar birthday, String email) throws CreationParameterNotValidException;

	public boolean deleteUser(String email);
}
