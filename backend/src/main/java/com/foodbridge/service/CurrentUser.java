package com.foodbridge.service;
import com.foodbridge.model.User; import com.foodbridge.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component;
@Component
public class CurrentUser {
 private final UserRepository users; public CurrentUser(UserRepository users){this.users=users;}
 public User get(){String email=SecurityContextHolder.getContext().getAuthentication().getName(); return users.findByEmail(email).orElseThrow();}
}
