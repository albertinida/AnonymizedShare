package it.uninsubria.dista.anonymizedshare.repositories;

import it.uninsubria.dista.anonymizedshare.models.Resource;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Resource.class)
public interface ResourcesRepository {
}
