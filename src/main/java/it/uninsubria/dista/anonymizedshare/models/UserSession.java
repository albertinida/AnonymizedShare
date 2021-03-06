package it.uninsubria.dista.anonymizedshare.models;

import javax.persistence.OneToOne;
import javax.persistence.Column;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class UserSession {
	
	@OneToOne
	private SocialUser socialUser;
	
	@Column(unique = true)
	private String sessionId;
	
}
