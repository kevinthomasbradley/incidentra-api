package com.kevinthomasbradley.incidentapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kevinthomasbradley.incidentapi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(User.Role role);
}
