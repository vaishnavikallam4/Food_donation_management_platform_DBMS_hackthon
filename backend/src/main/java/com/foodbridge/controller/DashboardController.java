package com.foodbridge.controller;

import com.foodbridge.model.*;
import com.foodbridge.repository.*;
import com.foodbridge.service.CurrentUser;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
 private final DonationRepository donations; private final FoodRequestRepository requests; private final UserRepository users; private final AssignmentRepository assignments; private final DistributionRepository distributions; private final CurrentUser current;
 public DashboardController(DonationRepository d, FoodRequestRepository r, UserRepository u, AssignmentRepository a, DistributionRepository ds, CurrentUser c){donations=d;requests=r;users=u;assignments=a;distributions=ds;current=c;}
 @GetMapping("/summary") public Map<String,Object> summary(){
  User me=current.get(); long totalDonations=0, available=0, pending=0, peopleServed=0, completed=0; double foodKg=0;
  if(me.getRole()==Role.DONOR){
   totalDonations=donations.countByDonorId(me.getId()); available=donations.countByDonorIdAndStatus(me.getId(),DonationStatus.AVAILABLE); pending=requests.countByDonationDonorIdAndStatus(me.getId(),RequestStatus.PENDING);
   List<Distribution> own=distributions.findAll().stream().filter(x->x.getFoodRequest().getDonation().getDonor().getId().equals(me.getId())).toList(); completed=own.size(); peopleServed=own.stream().mapToInt(x->x.getPeopleServed()==null?0:x.getPeopleServed()).sum(); foodKg=own.stream().mapToDouble(x->x.getDistributedKg()==null?0:x.getDistributedKg()).sum();
  } else if(me.getRole()==Role.NGO){
   totalDonations=donations.countByStatus(DonationStatus.AVAILABLE); available=totalDonations; pending=requests.countByNgoIdAndStatus(me.getId(),RequestStatus.PENDING);
   List<Distribution> own=distributions.findAll().stream().filter(x->x.getFoodRequest().getNgo().getId().equals(me.getId())).toList(); completed=own.size(); peopleServed=own.stream().mapToInt(x->x.getPeopleServed()==null?0:x.getPeopleServed()).sum(); foodKg=own.stream().mapToDouble(x->x.getDistributedKg()==null?0:x.getDistributedKg()).sum();
  } else if(me.getRole()==Role.VOLUNTEER){
   List<Assignment> own=assignments.findByVolunteerId(me.getId()); totalDonations=own.size(); available=(int)own.stream().filter(x->x.getStatus()==AssignmentStatus.PENDING).count(); pending=available; completed=own.stream().filter(x->x.getStatus()==AssignmentStatus.DELIVERED).count();
   List<Distribution> done=distributions.findAll().stream().filter(x->x.getFoodRequest()!=null && own.stream().anyMatch(a->a.getFoodRequest().getId().equals(x.getFoodRequest().getId()))).toList(); peopleServed=done.stream().mapToInt(x->x.getPeopleServed()==null?0:x.getPeopleServed()).sum(); foodKg=done.stream().mapToDouble(x->x.getDistributedKg()==null?0:x.getDistributedKg()).sum();
  }
  Map<String,Object> m=new LinkedHashMap<>(); m.put("totalDonations",totalDonations);m.put("availablePackages",available);m.put("pendingRequests",pending);m.put("completedDistributions",completed);m.put("foodDistributedKg",foodKg);m.put("peopleServed",peopleServed);m.put("donors",users.findByRole(Role.DONOR).size());m.put("ngos",users.findByRole(Role.NGO).size());m.put("volunteers",users.findByRole(Role.VOLUNTEER).size());m.put("role",me.getRole().name()); return m;
 }
}
