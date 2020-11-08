
package com.msbd.manmon.repository;

import com.msbd.manmon.domainmodel.EnvironmentStatus;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvironmentStatusJpaRepository extends JpaRepository<EnvironmentStatus, String> {
    List<EnvironmentStatus> findByTimeStartingWith(String dateHour);
    //void deleteFirst8640();
}
