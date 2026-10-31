package com.foodbridge.repository;
import com.foodbridge.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface FoodRequestRepository extends JpaRepository<FoodRequest,Long> {
 List<FoodRequest> findByNgoId(Long id);
 List<FoodRequest> findByDonationDonorId(Long donorId);
 long countByStatus(RequestStatus status);
 long countByDonationDonorIdAndStatus(Long donorId, RequestStatus status);
 long countByNgoIdAndStatus(Long ngoId, RequestStatus status);
}
