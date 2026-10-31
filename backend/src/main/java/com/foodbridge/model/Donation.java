package com.foodbridge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Donation {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private User donor;
 @Column(nullable=false) private String foodName;
 @Column(nullable=false) private String category;
 @Column(nullable=false) private Double quantityKg;
 private String foodType;
 private String description;
 @Column(nullable=false) private LocalDateTime preparedAt;
 @Column(nullable=false) private LocalDateTime expiryAt;
 @Column(nullable=false) private String pickupAddress;
 private String city;
 @Enumerated(EnumType.STRING) private DonationStatus status = DonationStatus.AVAILABLE;
 private LocalDateTime createdAt = LocalDateTime.now();
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public User getDonor(){return donor;} public void setDonor(User v){donor=v;}
 public String getFoodName(){return foodName;} public void setFoodName(String v){foodName=v;}
 public String getCategory(){return category;} public void setCategory(String v){category=v;}
 public Double getQuantityKg(){return quantityKg;} public void setQuantityKg(Double v){quantityKg=v;}
 public String getFoodType(){return foodType;} public void setFoodType(String v){foodType=v;}
 public String getDescription(){return description;} public void setDescription(String v){description=v;}
 public LocalDateTime getPreparedAt(){return preparedAt;} public void setPreparedAt(LocalDateTime v){preparedAt=v;}
 public LocalDateTime getExpiryAt(){return expiryAt;} public void setExpiryAt(LocalDateTime v){expiryAt=v;}
 public String getPickupAddress(){return pickupAddress;} public void setPickupAddress(String v){pickupAddress=v;}
 public String getCity(){return city;} public void setCity(String v){city=v;}
 public DonationStatus getStatus(){return status;} public void setStatus(DonationStatus v){status=v;}
 public LocalDateTime getCreatedAt(){return createdAt;}
}
