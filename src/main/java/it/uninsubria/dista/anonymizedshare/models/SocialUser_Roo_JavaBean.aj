// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package it.uninsubria.dista.anonymizedshare.models;

import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import java.math.BigInteger;

privileged aspect SocialUser_Roo_JavaBean {
    
    public BigInteger SocialUser.getUid() {
        return this.uid;
    }
    
    public void SocialUser.setUid(BigInteger uid) {
        this.uid = uid;
    }
    
    public String SocialUser.getName() {
        return this.name;
    }
    
    public void SocialUser.setName(String name) {
        this.name = name;
    }
    
    public String SocialUser.getSurname() {
        return this.surname;
    }
    
    public void SocialUser.setSurname(String surname) {
        this.surname = surname;
    }
    
    public String SocialUser.getEmail() {
        return this.email;
    }
    
    public void SocialUser.setEmail(String email) {
        this.email = email;
    }
    
    public String SocialUser.getPassword() {
        return this.password;
    }
    
    public void SocialUser.setPassword(String password) {
        this.password = password;
    }
    
    public String SocialUser.getSecret() {
        return this.secret;
    }
    
    public void SocialUser.setSecret(String secret) {
        this.secret = secret;
    }
    
}
