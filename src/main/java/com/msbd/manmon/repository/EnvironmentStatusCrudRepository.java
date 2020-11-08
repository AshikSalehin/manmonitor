
package com.msbd.manmon.repository;

import com.msbd.manmon.domainmodel.EnvironmentStatus;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvironmentStatusCrudRepository extends CrudRepository<EnvironmentStatus, String>{
    //void deleteFirst8640();
    
}
