package com.mois.authenticationservice.controller;


import com.mois.authenticationservice.model.Profile;
import com.mois.authenticationservice.repository.ProfileRepository;
import com.mois.authenticationservice.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
    @GetMapping("/")
    public void redirectToLocalhost(HttpServletResponse response, OAuth2AuthenticationToken oAuth2AuthenticationToken) throws IOException {
        Map<String, Object> attributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();
        String name = (String) attributes.get("name");
        Profile profile = profileService.getProfileByName(name);

        // Construct the redirection URL
        String redirectUrl = String.format("http://localhost:3000/%s", profile.getId());

        // Perform the redirection
        response.sendRedirect(redirectUrl);
    }
    @GetMapping("/me")
    public Map<String, Object> getUserProfile(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        Map<String, Object> userProfile = new HashMap<>();

        // Fetch user attributes from OAuth2 token
        Map<String, Object> attributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();
        String name = (String) attributes.get("name");
        Profile profile = profileService.getProfileByName(name);

        // Check if profile is found
        if (profile != null) {
            // Construct the welcome message
            String message = String.format("Welcome %s, your id is %s", name, profile.getId());

            // Populate the response map
            userProfile.put("id", profile.getId());
            userProfile.put("name", name);
            userProfile.put("message", message);
        } else {
            // Handle profile not found case
            userProfile.put("error", "Profile not found");
        }

        return userProfile;
    }



    @GetMapping("/secured")
    public String secured() {
        return "hello, secured";

    }


}