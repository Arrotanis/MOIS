package com.mois.authenticationservice.repository;

import com.mois.authenticationservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // Find a profile by email
    Profile findByEmail(String email);
}