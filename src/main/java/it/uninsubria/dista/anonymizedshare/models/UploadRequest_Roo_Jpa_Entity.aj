// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package it.uninsubria.dista.anonymizedshare.models;

import it.uninsubria.dista.anonymizedshare.models.UploadRequest;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect UploadRequest_Roo_Jpa_Entity {
    
    declare @type: UploadRequest: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_")
    private Long UploadRequest.id_;
    
    @Version
    @Column(name = "version")
    private Integer UploadRequest.version;
    
    public Long UploadRequest.getId_() {
        return this.id_;
    }
    
    public void UploadRequest.setId_(Long id) {
        this.id_ = id;
    }
    
    public Integer UploadRequest.getVersion() {
        return this.version;
    }
    
    public void UploadRequest.setVersion(Integer version) {
        this.version = version;
    }
    
}
