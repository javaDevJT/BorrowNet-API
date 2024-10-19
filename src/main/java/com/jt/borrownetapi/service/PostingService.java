package com.jt.borrownetapi.service;

import com.jt.borrownetapi.dto.PostingDTO;
import com.jt.borrownetapi.entity.Posting;
import com.jt.borrownetapi.entity.User;
import com.jt.borrownetapi.repository.PostingRepository;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PostingService {

    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private UserRepository userRepository;

    public PostingDTO getPostingById(Integer id) {
        Optional<Posting> postingOptional = postingRepository.findById(id);
        if (postingOptional.isPresent()) {
            return PostingDTO.fromPosting(postingOptional.get());
        } else {
            throw new EntityNotFoundException("Posting with id " + id + " does not exist.");
        }
    }

    public List<PostingDTO> getPostings(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Posting> pagedResult = postingRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent().stream().map(PostingDTO::fromPosting).toList();
        } else {
            return new ArrayList<PostingDTO>();
        }
    }

    public PostingDTO createPosting(PostingDTO postingDTO) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        Posting posting = Posting.fromPostingDTO(postingDTO);
        posting.setItemPhoto(PreferencesService.resizeImage(posting.getItemPhoto()));
        posting.setLender(userByEmail);
        posting = postingRepository.save(posting);
        userByEmail.addPosting(posting);
        userRepository.save(userByEmail);
        return PostingDTO.fromPosting(posting);
    }
}
