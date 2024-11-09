package com.jt.borrownetapi.service;

import com.jt.borrownetapi.dto.ItemRequestDTO;
import com.jt.borrownetapi.dto.PostingDTO;
import com.jt.borrownetapi.entity.ItemRequest;
import com.jt.borrownetapi.entity.Posting;
import com.jt.borrownetapi.entity.User;
import com.jt.borrownetapi.repository.ItemRequestRepository;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class PostingService {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

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

    public Page<PostingDTO> getPostings(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Posting> pagedResult = postingRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.map(PostingDTO::fromPosting);
        } else {
            return Page.empty();
        }
    }

    public PostingDTO createPosting(PostingDTO postingDTO) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        Posting posting = fromPostingDTO(postingDTO);
        posting.setItemPhoto(posting.getItemPhoto() != null ? PreferencesService.resizeImage(posting.getItemPhoto()) : null);
        posting.setLender(userByEmail);
        posting = postingRepository.save(posting);
        userByEmail.addPosting(posting);
        userRepository.save(userByEmail);
        return PostingDTO.fromPosting(posting);
    }

    public ItemRequestDTO createItemRequest(ItemRequestDTO itemRequestDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        itemRequestDTO.setRequester(userByEmail.getId());
        Posting posting = postingRepository.findById(itemRequestDTO.getPostingId()).get();
        Optional<ItemRequest> byRequesterAndPosting = itemRequestRepository.findByRequesterAndPostingAndRequestReviewed(userByEmail, posting, false);
        if (byRequesterAndPosting.isPresent()) {
            ItemRequest replaceItemRequest = byRequesterAndPosting.get();
            replaceItemRequest.setItemRequestLength(itemRequestDTO.getItemRequestLength());
            itemRequestRepository.save(replaceItemRequest);
            return ItemRequestDTO.fromItemRequest(replaceItemRequest);
        } else {
            ItemRequest itemRequest = fromItemRequestDTO(itemRequestDTO);
            itemRequestRepository.save(itemRequest);
            return ItemRequestDTO.fromItemRequest(itemRequest);
        }
    }

    public List<ItemRequestDTO> listRequestsForItem(Integer postingId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        Optional<Posting> posting = postingRepository.findById(postingId);
        if (posting.isEmpty() || !Objects.equals(posting.get().getLender().getId(), userByEmail.getId())) {
            throw new IllegalArgumentException("Either posting does not exist or requesting user does not own the requested posting.");
        }
        return itemRequestRepository.findByPosting_Id(postingId).stream().map(ItemRequestDTO::fromItemRequest).toList();
    }

    public Page<ItemRequestDTO> listUserRequests(Integer pageNo, Integer pageSize, String sortBy) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<ItemRequest> pagedResult = itemRequestRepository.findByPosting_Lender(userByEmail, paging);

        if(pagedResult.hasContent()) {
            return pagedResult.map(ItemRequestDTO::fromItemRequest);
        } else {
            return Page.empty();
        }
    }

    public ItemRequestDTO acceptRequest(Integer itemRequestId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        Optional<ItemRequest> byId = itemRequestRepository.findById(itemRequestId);
        if (byId.isEmpty() || !Objects.equals(byId.get().getPosting().getLender().getId(), userByEmail.getId())) {
            throw new IllegalArgumentException("Either item request does not exist or user does not own the posting.");
        }
        ItemRequest itemRequest = byId.get();
        itemRequest.setRequestAccepted(true);
        itemRequest.setRequestReviewed(true);
        itemRequestRepository.save(itemRequest);
        return ItemRequestDTO.fromItemRequest(itemRequest);
    }

    public ItemRequestDTO rejectRequest(Integer itemRequestId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        Optional<ItemRequest> byId = itemRequestRepository.findById(itemRequestId);
        if (byId.isEmpty() || !Objects.equals(byId.get().getPosting().getLender().getId(), userByEmail.getId())) {
            throw new IllegalArgumentException("Either item request does not exist or user does not own the posting.");
        }
        ItemRequest itemRequest = byId.get();
        itemRequest.setRequestAccepted(false);
        itemRequest.setRequestReviewed(true);
        itemRequestRepository.save(itemRequest);
        return ItemRequestDTO.fromItemRequest(itemRequest);
    }

    public Posting fromPostingDTO(PostingDTO postingDTO) {
        return Posting.builder().id(postingDTO.getId())
                .itemName(postingDTO.getItemName())
                .itemDescription(postingDTO.getItemDescription())
                .itemPhoto(postingDTO.getItemPhoto())
                .maxRentalDays(postingDTO.getMaxRentalDays())
                .condition(postingDTO.getCondition())
                .lender(postingDTO.getLender() != null ?
                        userRepository.findById(postingDTO.getLender()).get() :
                        null)
                .build();
    }

    public ItemRequest fromItemRequestDTO(ItemRequestDTO itemRequestDTO) {
        return ItemRequest.builder()
                .id(itemRequestDTO.getId())
                .posting(postingRepository.getReferenceById(itemRequestDTO.getPostingId()))
                .requester(userRepository.findById(itemRequestDTO.getRequester()).get())
                .itemRequestLength(itemRequestDTO.getItemRequestLength())
                .requestReviewed(itemRequestDTO.getRequestReviewed())
                .requestAccepted(itemRequestDTO.getRequestAccepted())
                .build();
    }
}
