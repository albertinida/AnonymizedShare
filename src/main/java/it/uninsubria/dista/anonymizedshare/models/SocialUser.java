package it.uninsubria.dista.anonymizedshare.models;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class SocialUser {
	
	@NotNull
	private String socialId;
	
	@NotNull
	private String name;
	
	@NotNull
	private String surname;
	
	@NotNull
	private Date birthday;
	
	@NotNull
	private String userSecret;
	
}
