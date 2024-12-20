package com.jt.borrownetapi.service;

import com.jt.borrownetapi.dto.PublicUserDTO;
import com.jt.borrownetapi.dto.RatingDTO;
import com.jt.borrownetapi.entity.Rating;
import com.jt.borrownetapi.entity.User;
import com.jt.borrownetapi.repository.RatingRepository;
import com.jt.borrownetapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class RatingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatingRepository ratingRepository;

    public PublicUserDTO rateUser(Integer targetId, RatingDTO ratingDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        Rating rating = fromRatingDTO(ratingDTO);
        Optional<User> targetUserOptional = userRepository.findById(targetId);
        if (targetUserOptional.isPresent()) {
            rating.setSubmitter(userByEmail);
            rating.setRatedUser(targetUserOptional.get());
            rating.getRatedUser().addRatingReceivedToProfile(rating);
            rating.getSubmitter().addRatingWrittenToProfile(rating);
            rating = ratingRepository.save(rating);
            return PublicUserDTO.fromUser(rating.getRatedUser());
        } else {
            throw new EntityNotFoundException("The target user for rating does not exist.");
        }
    }

    public Page<RatingDTO> getUserRatings(Integer targetId, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Rating> pagedResult = ratingRepository.findByRatedUser_Id(targetId, paging);

        if(pagedResult.hasContent()) {
            return pagedResult.map(RatingDTO::fromRating);
        } else {
            return Page.empty();
        }
    }

    public Rating fromRatingDTO(RatingDTO ratingDTO) {
        return Rating.builder().id(ratingDTO.getId())
                .ratedUser(ratingDTO.getRatedUser() != null ?
                        userRepository.findById(ratingDTO.getRatedUser()).get() :
                        null)
                .rating(ratingDTO.getRating())
                .submitter(ratingDTO.getSubmitter() != null ?
                        userRepository.findById(ratingDTO.getSubmitter()).get() :
                        null)
                .details(ratingDTO.getDetails())
                .build();
    }
}
