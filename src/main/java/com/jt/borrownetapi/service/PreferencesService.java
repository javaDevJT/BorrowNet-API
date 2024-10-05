package com.jt.borrownetapi.service;

import com.jt.borrownetapi.dto.UserPreferencesDTO;
import com.jt.borrownetapi.entity.User;
import com.jt.borrownetapi.entity.UserPreferences;
import com.jt.borrownetapi.repository.UserPreferencesRepository;
import com.jt.borrownetapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@Service
public class PreferencesService {
    @Autowired
    private UserPreferencesRepository userPreferencesRepository;

    @Autowired
    UserRepository userRepository;

    public UserPreferencesDTO getPrivateUserPreferencesDTO() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
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

    public UserPreferencesDTO updateUserPreferences(UserPreferencesDTO userPreferencesDTO) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        UserPreferences currentPreferences = userByEmail.getUserPreferences();
        currentPreferences.setProfilePicture(resizeImage(userPreferencesDTO.getProfilePicture()));
        currentPreferences.setProfileDescription(userPreferencesDTO.getProfileDescription());
        currentPreferences.setBorrowDistanceKM(userPreferencesDTO.getBorrowDistanceKM());
        return UserPreferencesDTO.fromUserPreferences(userPreferencesRepository.save(currentPreferences));
    }

    public static String resizeImage(String base64Image) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(base64Image);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        image = scaleImage(image, 500);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "JPG", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private static BufferedImage scaleImage(BufferedImage bufferedImage, int size) {
        double boundSize = size;
        int origWidth = bufferedImage.getWidth();
        int origHeight = bufferedImage.getHeight();
        double scale;
        if (origHeight > origWidth)
            scale = boundSize / origHeight;
        else
            scale = boundSize / origWidth;
        //* Don't scale up small images.
        if (scale > 1.0)
            return (bufferedImage);
        int scaledWidth = (int) (scale * origWidth);
        int scaledHeight = (int) (scale * origHeight);
        Image scaledImage = bufferedImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        // new ImageIcon(image); // load image
        // scaledWidth = scaledImage.getWidth(null);
        // scaledHeight = scaledImage.getHeight(null);
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaledBI.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
        return (scaledBI);
    }
}
