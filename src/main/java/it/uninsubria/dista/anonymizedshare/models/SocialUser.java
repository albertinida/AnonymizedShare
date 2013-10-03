package it.uninsubria.dista.anonymizedshare.models;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class SocialUser {
	
	@NotNull
	private String name;
	
	@NotNull
	private String surname;
	
	@NotNull
	private Calendar birthday;
	
	@NotNull
    @Column(unique = true)
	private String email;
	
	/* campo gestito da AS-Server (ex-RMS)
	 * serve per memorizzare la chiave segreta UNIVOCA per utente
	 * da generare pseudorandom alla creazione
	 */
	@NotNull
    @Column(unique = true)
	private String userSecret;
	
}
