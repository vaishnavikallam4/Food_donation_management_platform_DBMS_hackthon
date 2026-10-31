package com.foodbridge.controller;

import com.foodbridge.model.*; import com.foodbridge.repository.*; import com.foodbridge.service.CurrentUser;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.time.*; import java.util.*;

@RestController @RequestMapping("/api/assignments")
public class AssignmentController {
 private final AssignmentRepository assignments;private final FoodRequestRepository requests;private final UserRepository users;private final DistributionRepository distributions;private final CurrentUser current;
 public AssignmentController(AssignmentRepository a,FoodRequestRepository r,UserRepository u,DonationRepository d,DistributionRepository ds,CurrentUser c){assignments=a;requests=r;users=u;distributions=ds;current=c;}
 @GetMapping public List<Assignment> all(){User me=current.get();if(me.getRole()==Role.VOLUNTEER)return assignments.findByVolunteerId(me.getId());if(me.getRole()==Role.DONOR)return assignments.findAll().stream().filter(a->a.getFoodRequest().getDonation().getDonor().getId().equals(me.getId())).toList();if(me.getRole()==Role.NGO)return assignments.findAll().stream().filter(a->a.getFoodRequest().getNgo().getId().equals(me.getId())).toList();return List.of();}
 @PostMapping public ResponseEntity<?> create(@RequestParam Long requestId,@RequestParam Long volunteerId){
  User donor=current.get();if(donor.getRole()!=Role.DONOR)return ResponseEntity.status(403).body(Map.of("message","Only the donor can assign a volunteer"));
  FoodRequest r=requests.findById(requestId).orElse(null);User v=users.findById(volunteerId).orElse(null);
  if(r==null||!r.getDonation().getDonor().getId().equals(donor.getId()))return ResponseEntity.status(404).body(Map.of("message","Request not found for your donations"));
  if(r.getStatus()!=RequestStatus.APPROVED)return ResponseEntity.badRequest().body(Map.of("message","Accept the NGO request before assigning a volunteer"));
  if(v==null||v.getRole()!=Role.VOLUNTEER)return ResponseEntity.badRequest().body(Map.of("message","Choose a valid volunteer"));
  if(assignments.findByFoodRequestId(requestId).isPresent())return ResponseEntity.badRequest().body(Map.of("message","A volunteer is already assigned to this request"));
  Assignment a=new Assignment();a.setFoodRequest(r);a.setVolunteer(v);return ResponseEntity.status(201).body(assignments.save(a));
 }
 @PutMapping("/{id}/status") public ResponseEntity<?> update(@PathVariable Long id,@RequestParam AssignmentStatus status){
  Assignment a=assignments.findById(id).orElse(null);if(a==null)return ResponseEntity.notFound().build();User me=current.get();
  if(me.getRole()!=Role.VOLUNTEER||!a.getVolunteer().getId().equals(me.getId()))return ResponseEntity.status(403).body(Map.of("message","Only the assigned volunteer can update delivery status"));
  if(status==AssignmentStatus.ACCEPTED){if(a.getStatus()!=AssignmentStatus.PENDING)return ResponseEntity.badRequest().body(Map.of("message","This assignment is not awaiting acceptance"));}
  else if(status==AssignmentStatus.PICKED_UP){if(a.getStatus()!=AssignmentStatus.ACCEPTED)return ResponseEntity.badRequest().body(Map.of("message","Accept the assignment before pickup"));a.getFoodRequest().getDonation().setStatus(DonationStatus.PICKED_UP);}
  else if(status==AssignmentStatus.DELIVERED){if(a.getStatus()!=AssignmentStatus.PICKED_UP)return ResponseEntity.badRequest().body(Map.of("message","Mark the food as picked up first"));a.setCompletedAt(LocalDateTime.now());a.getFoodRequest().getDonation().setStatus(DonationStatus.DISTRIBUTED);Distribution d=new Distribution();d.setFoodRequest(a.getFoodRequest());d.setDistributedKg(a.getFoodRequest().getRequestedKg());d.setPeopleServed(a.getFoodRequest().getPeopleToServe());distributions.save(d);}
  else return ResponseEntity.badRequest().body(Map.of("message","Invalid volunteer status update"));
  a.setStatus(status);return ResponseEntity.ok(assignments.save(a));
 }
}
