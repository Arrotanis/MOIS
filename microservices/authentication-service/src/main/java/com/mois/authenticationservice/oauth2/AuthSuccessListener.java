package com.mois.authenticationservice.oauth2;

import com.mois.authenticationservice.model.Profile;
import com.mois.authenticationservice.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

@Component
public class AuthSuccessListener {
    private final ProfileService profileService;
    private final Logger logger = LoggerFactory.getLogger(AuthSuccessListener.class);
    @Autowired
    public AuthSuccessListener(ProfileService profileService) {
        this.profileService = profileService;
    }

    @EventListener
    public void handleAuthenticationSuccess(InteractiveAuthenticationSuccessEvent event) {
        if (event.getAuthentication() instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) event.getAuthentication();
            OAuth2User user = authToken.getPrincipal();
            String name = user.getAttribute("name");
            String email = user.getAttribute("email");
            String fbId = user.getAttribute("id");

            // Check if a user with the same name already exists
            Profile existingProfile = profileService.getProfileByName(name);
            if (existingProfile != null) {
                // Print existing profile details
                logger.info("Existing Profile:");
                logger.info("ID: " + existingProfile.getId());
                logger.info("Name: " + existingProfile.getName());
                logger.info("Email: " + existingProfile.getEmail());
                profileService.saveProfile(existingProfile);
            } else {
                // User with the same name doesn't exist, create a new profile
                Profile profile = new Profile(112121L, name, email, fbId);
                profileService.saveProfile(profile);
            }
        }
    }
}