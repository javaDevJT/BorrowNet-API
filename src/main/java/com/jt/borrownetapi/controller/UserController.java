package com.jt.borrownetapi.controller;

import com.jt.borrownetapi.dto.PublicUserDTO;
import com.jt.borrownetapi.dto.RatingDTO;
import com.jt.borrownetapi.dto.ReportDTO;
import com.jt.borrownetapi.dto.UserDTO;
import com.jt.borrownetapi.service.RatingService;
import com.jt.borrownetapi.service.ReportService;
import com.jt.borrownetapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private ReportService reportService;

    @GetMapping("public/{id}")
    public PublicUserDTO getPublicUser(@PathVariable("id") Integer id) {
        PublicUserDTO foundUser = userService.getPublicUserById(id);
        if (foundUser == null) {
            throw new EntityNotFoundException("User does not exist.");
        } else {
            return foundUser;
        }
    }

    @GetMapping("public/list")
    public Page<PublicUserDTO> getPublicUserList(@RequestParam(defaultValue = "0") Integer pageNo,
                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                 @RequestParam(defaultValue = "id") String sortBy) {
        Page<PublicUserDTO> foundUserList = userService.getPublicUserList(pageNo, pageSize, sortBy);
        if (foundUserList == null && !foundUserList.isEmpty()) {
            throw new EntityNotFoundException("Users do not exist.");
        } else {
            return foundUserList;
        }
    }

    @GetMapping
    public UserDTO getPrivateUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return userService.getPrivateUserDTO();
        }
    }

    @Transactional
    @PostMapping("{id}/rate")
    public ResponseEntity<PublicUserDTO> rateUser(@PathVariable("id") Integer targetId, @RequestBody RatingDTO ratingDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(ratingService.rateUser(targetId, ratingDTO), HttpStatus.CREATED);
        }
    }

    @Transactional
    @GetMapping("{id}/rate")
    public ResponseEntity<Page<RatingDTO>> getUserRatings(@PathVariable("id") Integer targetId,
                                                          @RequestParam(defaultValue = "0") Integer pageNo,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @RequestParam(defaultValue = "id") String sortBy) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(ratingService.getUserRatings(targetId, pageNo, pageSize, sortBy), HttpStatus.OK);
        }
    }

    @Transactional
    @PostMapping("{id}/report")
    public ResponseEntity<PublicUserDTO> reportUser(@PathVariable("id") Integer targetId, @RequestBody ReportDTO reportDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(reportService.reportUser(targetId, reportDTO), HttpStatus.CREATED);
        }
    }
}