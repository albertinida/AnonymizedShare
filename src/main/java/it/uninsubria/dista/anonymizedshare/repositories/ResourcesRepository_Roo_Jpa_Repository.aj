// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package it.uninsubria.dista.anonymizedshare.repositories;

import it.uninsubria.dista.anonymizedshare.models.Resource;
import it.uninsubria.dista.anonymizedshare.repositories.ResourcesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect ResourcesRepository_Roo_Jpa_Repository {
    
    declare parents: ResourcesRepository extends JpaRepository<Resource, Long>;
    
    declare parents: ResourcesRepository extends JpaSpecificationExecutor<Resource>;
    
    declare @type: ResourcesRepository: @Repository;
    
}
