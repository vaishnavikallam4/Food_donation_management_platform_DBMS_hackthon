package com.foodbridge.controller;
import com.foodbridge.model.*;import com.foodbridge.repository.*;import com.foodbridge.service.CurrentUser;
import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/users")
public class UserController {
 private final UserRepository users;private final CurrentUser current;
 public UserController(UserRepository u,CurrentUser c){users=u;current=c;}
 @GetMapping("/volunteers") public ResponseEntity<?> volunteers(){if(current.get().getRole()!=Role.DONOR)return ResponseEntity.status(403).build();return ResponseEntity.ok(users.findByRole(Role.VOLUNTEER).stream().map(u->Map.of("id",u.getId(),"name",u.getName(),"email",u.getEmail(),"phone",u.getPhone()==null?"":u.getPhone(),"role",u.getRole())).toList());}
}
