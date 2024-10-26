package com.jt.borrownetapi.service;


import com.jt.borrownetapi.dto.PublicUserDTO;
import com.jt.borrownetapi.dto.UserDTO;
import com.jt.borrownetapi.entity.User;
import com.jt.borrownetapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email);
        log.debug(String.valueOf(user));

        if(user==null) {
            throw new UsernameNotFoundException("User not found with this email " + email);

        }


        log.debug("Loaded user: " + user.getEmail() + ", Role: " + user.getRole());
        return user;
    }

    public PublicUserDTO getPublicUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return PublicUserDTO.fromUser(user);
        } else {
            return null;
        }
    }

    public Page<PublicUserDTO> getPublicUserList(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<User> pagedResult = userRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.map(PublicUserDTO::fromUser);
        } else {
            return Page.empty();
        }
    }

    public UserDTO getPrivateUserDTO() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        return UserDTO.fromUser(userByEmail);
    }
}
