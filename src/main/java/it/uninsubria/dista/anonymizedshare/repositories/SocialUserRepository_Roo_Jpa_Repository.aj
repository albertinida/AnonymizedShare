// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package it.uninsubria.dista.anonymizedshare.repositories;

import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.repositories.SocialUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect SocialUserRepository_Roo_Jpa_Repository {
    
    declare parents: SocialUserRepository extends JpaRepository<SocialUser, Long>;
    
    declare parents: SocialUserRepository extends JpaSpecificationExecutor<SocialUser>;
    
    declare @type: SocialUserRepository: @Repository;
    
}
