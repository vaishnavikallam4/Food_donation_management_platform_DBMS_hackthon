package com.foodbridge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Distribution {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @OneToOne(optional=false) private FoodRequest foodRequest;
 private Double distributedKg;
 private Integer peopleServed;
 private String proofReference;
 private LocalDateTime distributedAt = LocalDateTime.now();
 public Long getId(){return id;} public FoodRequest getFoodRequest(){return foodRequest;} public void setFoodRequest(FoodRequest v){foodRequest=v;}
 public Double getDistributedKg(){return distributedKg;} public void setDistributedKg(Double v){distributedKg=v;}
 public Integer getPeopleServed(){return peopleServed;} public void setPeopleServed(Integer v){peopleServed=v;}
 public String getProofReference(){return proofReference;} public void setProofReference(String v){proofReference=v;}
 public LocalDateTime getDistributedAt(){return distributedAt;}
}
