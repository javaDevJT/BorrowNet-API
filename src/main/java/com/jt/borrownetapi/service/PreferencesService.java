package com.jt.borrownetapi.service;

import com.jt.borrownetapi.dto.UserPreferencesDTO;
import com.jt.borrownetapi.entity.User;
import com.jt.borrownetapi.entity.UserPreferences;
import com.jt.borrownetapi.entity.UserPreferencesRepository;
import com.jt.borrownetapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PreferencesService {
    @Autowired
    private UserPreferencesRepository userPreferencesRepository;

    @Autowired
    UserRepository userRepository;

    public UserPreferencesDTO getPrivateUserPreferencesDTO() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmail(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        return UserPreferencesDTO.builder()
                        .id(userByEmail.getUserPreferences().getId())
                        .borrowDistanceKM(userByEmail.getUserPreferences().getBorrowDistanceKM())
                        .profilePicture(userByEmail.getUserPreferences().getProfilePicture())
                        .profileDescription(userByEmail.getUserPreferences().getProfileDescription())
                        .build();
    }

    public UserPreferencesDTO updateUserPreferences(UserPreferencesDTO userPreferencesDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmail(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        UserPreferences currentPreferences = userByEmail.getUserPreferences();
        currentPreferences.setProfilePicture(userPreferencesDTO.getProfilePicture());
        currentPreferences.setProfileDescription(userPreferencesDTO.getProfileDescription());
        currentPreferences.setBorrowDistanceKM(userPreferencesDTO.getBorrowDistanceKM());
        return UserPreferencesDTO.fromUserPreferences(userPreferencesRepository.save(currentPreferences));
    }
}
