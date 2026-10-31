package com.foodbridge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
public class User {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String name;
 @Column(nullable=false, unique=true) private String email;
 @Column(nullable=false) private String password;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role;
 private String phone;
 private String organization;
 private String address;
 private boolean verified = false;
 private LocalDateTime createdAt = LocalDateTime.now();
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public String getName(){return name;} public void setName(String v){name=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getPassword(){return password;} public void setPassword(String v){password=v;}
 public Role getRole(){return role;} public void setRole(Role v){role=v;}
 public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
 public String getOrganization(){return organization;} public void setOrganization(String v){organization=v;}
 public String getAddress(){return address;} public void setAddress(String v){address=v;}
 public boolean isVerified(){return verified;} public void setVerified(boolean v){verified=v;}
 public LocalDateTime getCreatedAt(){return createdAt;}
}
