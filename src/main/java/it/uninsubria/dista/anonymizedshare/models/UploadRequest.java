package it.uninsubria.dista.anonymizedshare.models;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class UploadRequest {

	@NotNull
	@Column(unique = true)
	private BigInteger id;
	
	@NotNull
	private int number;
	
	@NotNull
	@ManyToOne
	SocialUser socialUser;
	
}
