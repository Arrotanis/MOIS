package com.mois.authenticationservice.service;

import com.mois.authenticationservice.model.Profile;
import com.mois.authenticationservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService extends DefaultOAuth2UserService {

    private final ProfileRepository profileRepository;

    // Update an existing profile
    public Optional<Profile> updateProfile(Long id, Profile profileDetails) {
        return profileRepository.findById(id).map(profile -> {
            profile.setFirstName(profileDetails.getFirstName());
            profile.setLastName(profileDetails.getLastName());
            profile.setEmail(profileDetails.getEmail());
            profile.setPhoneNumber(profileDetails.getPhoneNumber());
            profile.setFacebookId(profileDetails.getFacebookId());
            profile.setAddress(profileDetails.getAddress());
            profile.setZipCode(profileDetails.getZipCode());
            profile.setCity(profileDetails.getCity());
            profile.setCountry(profileDetails.getCountry());
            profile.setProfileImageUrl(profileDetails.getProfileImageUrl());
            return profileRepository.save(profile);
        });
    }

    // Retrieve a profile by ID
    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }
    // Save a new or update an existing profile
    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    // Delete a profile by ID
    public boolean deleteProfile(Long id) {
        return profileRepository.findById(id)
                .map(profile -> {
                    profileRepository.delete(profile);
                    return true;
                }).orElse(false);
    }
    // Retrieve all profiles
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Profile processOAuth2User(OAuth2User user) {
        // Extract data from Facebook user
        String email = user.getAttribute("email");
        String name = user.getAttribute("name");
        String facebookId = user.getName(); // Facebook user ID is the default user name attribute

        // Check if user exists and update or create new user accordingly
        Profile profile = profileRepository.findByEmail(email);
        if (profile == null) {
            profile = new Profile();
            profile.setEmail(email);
            profile.setFacebookId(facebookId);
            profile.setFirstName(name.split(" ")[0]);
            if(name.split(" ").length > 1) {
                profile.setLastName(name.split(" ")[1]);
            }
            profile = profileRepository.save(profile);
        }

        return profile;
    }
}