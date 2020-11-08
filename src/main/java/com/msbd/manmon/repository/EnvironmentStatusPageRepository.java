
package com.msbd.manmon.repository;

import com.msbd.manmon.domainmodel.EnvironmentStatus;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface EnvironmentStatusPageRepository extends PagingAndSortingRepository<EnvironmentStatus, String> {

}
