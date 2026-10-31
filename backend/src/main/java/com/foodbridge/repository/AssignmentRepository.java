package com.foodbridge.repository;
import com.foodbridge.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
 List<Assignment> findByVolunteerId(Long id);
 Optional<Assignment> findByFoodRequestId(Long requestId);
}
