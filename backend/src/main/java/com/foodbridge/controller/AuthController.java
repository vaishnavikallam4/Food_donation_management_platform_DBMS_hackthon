package com.foodbridge.controller;

import com.foodbridge.dto.*; import com.foodbridge.model.*; import com.foodbridge.repository.UserRepository; import com.foodbridge.security.JwtService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*; import java.util.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthController(UserRepository u,PasswordEncoder e,JwtService j){users=u;encoder=e;jwt=j;}
 @PostMapping("/register") public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest r){
  if(users.existsByEmail(r.email())) return ResponseEntity.badRequest().body(Map.of("message","Email already registered"));
  Role role; try{role=Role.valueOf(r.role().toUpperCase());}catch(Exception e){return ResponseEntity.badRequest().body(Map.of("message","Invalid role"));}
  if(role==Role.ADMIN) return ResponseEntity.badRequest().body(Map.of("message","Admin accounts must be promoted by the database owner"));
  User u=new User();u.setName(r.name());u.setEmail(r.email().toLowerCase());u.setPassword(encoder.encode(r.password()));u.setRole(role);
  u.setPhone(r.phone());u.setOrganization(r.organization());u.setAddress(r.address());users.save(u);
  return ResponseEntity.status(201).body(Map.of("message","Registration successful. Please log in."));
 }
 @PostMapping("/login") public ResponseEntity<?> login(@Valid @RequestBody AuthRequest r){
  User u=users.findByEmail(r.email().toLowerCase()).orElse(null);
  if(u==null||!encoder.matches(r.password(),u.getPassword())) return ResponseEntity.status(401).body(Map.of("message","Invalid email or password"));
  return ResponseEntity.ok(Map.of("token",jwt.generate(u.getEmail(),u.getRole().name()),"user",safe(u)));
 }
 public static Map<String,Object> safe(User u){return Map.of("id",u.getId(),"name",u.getName(),"email",u.getEmail(),"role",u.getRole(),"phone",u.getPhone()==null?"":u.getPhone(),"organization",u.getOrganization()==null?"":u.getOrganization(),"address",u.getAddress()==null?"":u.getAddress(),"verified",u.isVerified());}
}
