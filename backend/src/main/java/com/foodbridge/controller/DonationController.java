package com.foodbridge.controller;

import com.foodbridge.dto.DonationRequest; import com.foodbridge.model.*; import com.foodbridge.repository.*;
import com.foodbridge.service.CurrentUser; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime; import java.util.*;

@RestController @RequestMapping("/api/donations")
public class DonationController {
 private final DonationRepository donations; private final UserRepository users; private final CurrentUser current;
 public DonationController(DonationRepository d,UserRepository u,CurrentUser c){donations=d;users=u;current=c;}
 @GetMapping public List<Donation> all(@RequestParam(required=false) String status,@RequestParam(required=false) String q){
  User me=current.get(); List<Donation> list;
  if(me.getRole()==Role.ADMIN) list=donations.findAll();
  else if(me.getRole()==Role.DONOR) list=donations.findByDonorId(me.getId());
  else if(me.getRole()==Role.NGO) list=donations.findByStatus(DonationStatus.AVAILABLE);
  else list=List.of();
  if(status!=null&&!status.isBlank()) list=list.stream().filter(d->d.getStatus().name().equalsIgnoreCase(status)).toList();
  if(q!=null&&!q.isBlank()) list=list.stream().filter(d->d.getFoodName().toLowerCase().contains(q.toLowerCase())||d.getCategory().toLowerCase().contains(q.toLowerCase())).toList();
  return list;
 }
 @PostMapping public ResponseEntity<?> create(@Valid @RequestBody DonationRequest r){
  User me=current.get(); if(me.getRole()!=Role.DONOR&&me.getRole()!=Role.ADMIN)return ResponseEntity.status(403).body(Map.of("message","Only donors can create donations"));
  if(!r.expiryAt().isAfter(LocalDateTime.now()))return ResponseEntity.badRequest().body(Map.of("message","Expiry must be in the future"));
  if(r.expiryAt().isBefore(r.preparedAt()))return ResponseEntity.badRequest().body(Map.of("message","Expiry must be after preparation"));
  Donation d=new Donation();d.setDonor(me);d.setFoodName(r.foodName());d.setCategory(r.category());d.setQuantityKg(r.quantityKg());d.setFoodType(r.foodType());d.setDescription(r.description());d.setPreparedAt(r.preparedAt());d.setExpiryAt(r.expiryAt());d.setPickupAddress(r.pickupAddress());d.setCity(r.city());return ResponseEntity.status(201).body(donations.save(d));
 }
 @GetMapping("/{id}") public ResponseEntity<?> one(@PathVariable Long id){
  Donation d=donations.findById(id).orElse(null);if(d==null)return ResponseEntity.notFound().build();
  User me=current.get();
  if(me.getRole()!=Role.ADMIN && me.getRole()!=Role.NGO && !d.getDonor().getId().equals(me.getId()))return ResponseEntity.status(403).build();
  if(me.getRole()==Role.NGO && d.getStatus()!=DonationStatus.AVAILABLE)return ResponseEntity.status(403).build();
  return ResponseEntity.ok(d);
 }
 @PutMapping("/{id}/status") public ResponseEntity<?> status(@PathVariable Long id,@RequestParam DonationStatus status){
  Donation d=donations.findById(id).orElse(null);if(d==null)return ResponseEntity.notFound().build();
  User me=current.get();if(me.getRole()!=Role.ADMIN&&!d.getDonor().getId().equals(me.getId()))return ResponseEntity.status(403).build();
  d.setStatus(status);return ResponseEntity.ok(donations.save(d));
 }
}
