// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package it.uninsubria.dista.anonymizedshare.repositories;

import it.uninsubria.dista.anonymizedshare.models.UploadRequest;
import it.uninsubria.dista.anonymizedshare.repositories.UploadRequestRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect UploadRequestRepository_Roo_Jpa_Repository {
    
    declare parents: UploadRequestRepository extends JpaRepository<UploadRequest, Long>;
    
    declare parents: UploadRequestRepository extends JpaSpecificationExecutor<UploadRequest>;
    
    declare @type: UploadRequestRepository: @Repository;
    
}
