
package com.msbd.manmon.repository;

import com.msbd.manmon.domainmodel.LastRelayStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelayStatusCrudRepository extends CrudRepository<LastRelayStatus, String>{
    
}
