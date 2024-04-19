package com.mois.authenticationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 15)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String facebookId; // Unique identifier from Facebook

    // Additional fields
    @Column(length = 100)
    private String address;

    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String country;

    // Profile image URL, typically obtained from Facebook
    @Column(length = 255)
    private String profileImageUrl;
}