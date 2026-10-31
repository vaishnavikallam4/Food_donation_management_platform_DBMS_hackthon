package com.foodbridge.controller;

import com.foodbridge.dto.FoodRequestDto; import com.foodbridge.model.*; import com.foodbridge.repository.*; import com.foodbridge.service.CurrentUser;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.time.*; import java.util.*;

@RestController @RequestMapping("/api/requests")
public class RequestController {
 private final FoodRequestRepository requests; private final DonationRepository donations; private final CurrentUser current;
 public RequestController(FoodRequestRepository r,DonationRepository d,UserRepository u,CurrentUser c){requests=r;donations=d;current=c;}
 @GetMapping public List<FoodRequest> all(){
  User me=current.get();
  if(me.getRole()==Role.NGO)return requests.findByNgoId(me.getId());
  if(me.getRole()==Role.DONOR)return requests.findByDonationDonorId(me.getId());
  return List.of();
 }
 @PostMapping public ResponseEntity<?> create(@Valid @RequestBody FoodRequestDto dto){
  User me=current.get();if(me.getRole()!=Role.NGO)return ResponseEntity.status(403).body(Map.of("message","Only NGOs can request food"));
  Donation d=donations.findById(dto.donationId()).orElse(null);if(d==null||d.getStatus()!=DonationStatus.AVAILABLE)return ResponseEntity.badRequest().body(Map.of("message","Donation is not available"));
  if(dto.requestedKg()==null||dto.requestedKg()<=0||dto.requestedKg()>d.getQuantityKg())return ResponseEntity.badRequest().body(Map.of("message","Requested quantity must be positive and cannot exceed available quantity"));
  if(d.getDonor().getId().equals(me.getId()))return ResponseEntity.badRequest().body(Map.of("message","You cannot request your own donation"));
  FoodRequest r=new FoodRequest();r.setNgo(me);r.setDonation(d);r.setRequestedKg(dto.requestedKg());r.setPeopleToServe(dto.peopleToServe());r.setPurpose(dto.purpose());r.setUrgency(dto.urgency()==null?"NORMAL":dto.urgency().toUpperCase());
  int score="HIGH".equals(r.getUrgency())?30:"MEDIUM".equals(r.getUrgency())?20:10;long hours=Duration.between(LocalDateTime.now(),d.getExpiryAt()).toHours();if(hours<24)score+=40;else if(hours<48)score+=20;r.setPriorityScore(score);
  return ResponseEntity.status(201).body(requests.save(r));
 }
 @PutMapping("/{id}/decision") public ResponseEntity<?> decision(@PathVariable Long id,@RequestParam RequestStatus status){
  User me=current.get();FoodRequest r=requests.findById(id).orElse(null);if(r==null)return ResponseEntity.notFound().build();
  if(me.getRole()!=Role.DONOR||!r.getDonation().getDonor().getId().equals(me.getId()))return ResponseEntity.status(403).body(Map.of("message","Only the donor who published this donation can decide this request"));
  if(status!=RequestStatus.APPROVED&&status!=RequestStatus.REJECTED)return ResponseEntity.badRequest().body(Map.of("message","Decision must be APPROVED or REJECTED"));
  if(r.getStatus()!=RequestStatus.PENDING)return ResponseEntity.badRequest().body(Map.of("message","This request has already been processed"));
  if(status==RequestStatus.APPROVED){Donation d=r.getDonation();if(d.getStatus()!=DonationStatus.AVAILABLE)return ResponseEntity.badRequest().body(Map.of("message","This donation is no longer available"));if(r.getRequestedKg()>d.getQuantityKg())return ResponseEntity.badRequest().body(Map.of("message","Requested quantity is no longer available"));d.setStatus(DonationStatus.RESERVED);}
  r.setStatus(status);return ResponseEntity.ok(requests.save(r));
 }
}
