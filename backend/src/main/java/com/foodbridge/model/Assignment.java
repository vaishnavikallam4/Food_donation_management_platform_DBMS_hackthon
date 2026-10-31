package com.foodbridge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Assignment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @OneToOne(optional=false) private FoodRequest foodRequest;
 @ManyToOne(optional=false) private User volunteer;
 @Enumerated(EnumType.STRING) private AssignmentStatus status = AssignmentStatus.PENDING;
 private LocalDateTime assignedAt = LocalDateTime.now();
 private LocalDateTime completedAt;
 public Long getId(){return id;} public FoodRequest getFoodRequest(){return foodRequest;} public void setFoodRequest(FoodRequest v){foodRequest=v;}
 public User getVolunteer(){return volunteer;} public void setVolunteer(User v){volunteer=v;}
 public AssignmentStatus getStatus(){return status;} public void setStatus(AssignmentStatus v){status=v;}
 public LocalDateTime getAssignedAt(){return assignedAt;} public LocalDateTime getCompletedAt(){return completedAt;} public void setCompletedAt(LocalDateTime v){completedAt=v;}
}
