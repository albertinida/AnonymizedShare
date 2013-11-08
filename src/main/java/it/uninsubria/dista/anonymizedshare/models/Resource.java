package it.uninsubria.dista.anonymizedshare.models;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class Resource {
	
	@NotNull
	@ManyToOne
	private SocialUser userOwner;
	
	@NotNull
	private String name;
	
	@NotNull
	private String mimeType;
	
	@NotNull
	private long size;
	
	@NotNull
	private Date lastModification;
	
	@NotNull
//TODO: inject property	@Max("${pathfinder.maxdepth}")
	private int sharingDepth;
	
}
