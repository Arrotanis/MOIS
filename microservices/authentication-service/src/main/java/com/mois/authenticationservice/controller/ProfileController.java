package com.mois.authenticationservice.controller;


import com.mois.authenticationservice.model.Profile;
import com.mois.authenticationservice.repository.ProfileRepository;
import com.mois.authenticationservice.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
    @GetMapping("/")
    public void redirectToLocalhost(HttpServletResponse response) throws IOException {
        // Perform the redirection
        response.sendRedirect("http://localhost:3000");
    }
    @GetMapping("/me")
    public Map<String, Object> getUserProfile(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        Map<String, Object> userProfile = new HashMap<>();

        // Fetch user attributes from OAuth2 token
        Map<String, Object> attributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();
        String name = (String) attributes.get("name");
        Profile profile = profileService.getProfileByName(name);

        // Construct the welcome message
        String message = String.format("Welcome %s, Your id is %s", name, profile.getId());

        return userProfile;
    }



    @GetMapping("/secured")
    public String secured() {
        return "hello, secured";

    }


}