// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package it.uninsubria.dista.anonymizedshare.models;

import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect SocialUser_Roo_Jpa_Entity {
    
    declare @type: SocialUser: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long SocialUser.id;
    
    @Version
    @Column(name = "version")
    private Integer SocialUser.version;
    
    public Long SocialUser.getId() {
        return this.id;
    }
    
    public void SocialUser.setId(Long id) {
        this.id = id;
    }
    
    public Integer SocialUser.getVersion() {
        return this.version;
    }
    
    public void SocialUser.setVersion(Integer version) {
        this.version = version;
    }
    
}
