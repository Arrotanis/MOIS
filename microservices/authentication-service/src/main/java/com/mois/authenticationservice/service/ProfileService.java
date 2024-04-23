package com.mois.authenticationservice.service;

import com.mois.authenticationservice.model.Profile;
import com.mois.authenticationservice.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService extends DefaultOAuth2UserService {
    private final ProfileRepository profileRepository;

    // Retrieve a profile by ID
    public Profile getProfileById(Long id) {
        return profileRepository.findById(id)
                .orElse(null); // Handle null case appropriately
    }

    // Retrieve a profile by name
    public Profile getProfileByName(String name) {
        return profileRepository.findByName(name)
                .orElse(null); // Handle null case appropriately
    }

    // Save a new or update an existing profile
    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }
}