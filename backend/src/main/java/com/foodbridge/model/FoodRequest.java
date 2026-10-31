package com.foodbridge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="food_requests")
public class FoodRequest {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private User ngo;
 @ManyToOne(optional=false) private Donation donation;
 private Double requestedKg;
 private Integer peopleToServe;
 private String purpose;
 private String urgency = "NORMAL";
 @Enumerated(EnumType.STRING) private RequestStatus status = RequestStatus.PENDING;
 private Integer priorityScore = 0;
 private LocalDateTime createdAt = LocalDateTime.now();
 public Long getId(){return id;} public User getNgo(){return ngo;} public void setNgo(User v){ngo=v;}
 public Donation getDonation(){return donation;} public void setDonation(Donation v){donation=v;}
 public Double getRequestedKg(){return requestedKg;} public void setRequestedKg(Double v){requestedKg=v;}
 public Integer getPeopleToServe(){return peopleToServe;} public void setPeopleToServe(Integer v){peopleToServe=v;}
 public String getPurpose(){return purpose;} public void setPurpose(String v){purpose=v;}
 public String getUrgency(){return urgency;} public void setUrgency(String v){urgency=v;}
 public RequestStatus getStatus(){return status;} public void setStatus(RequestStatus v){status=v;}
 public Integer getPriorityScore(){return priorityScore;} public void setPriorityScore(Integer v){priorityScore=v;}
 public LocalDateTime getCreatedAt(){return createdAt;}
}
