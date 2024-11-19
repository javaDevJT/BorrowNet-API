package com.jt.borrownetapi.controller;

import com.jt.borrownetapi.dto.ItemRequestDTO;
import com.jt.borrownetapi.dto.PostingDTO;
import com.jt.borrownetapi.service.PostingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostingController {
    @Autowired
    private PostingService postingService;

    @GetMapping("/{id}")
    public ResponseEntity<PostingDTO> getPostById(@PathVariable("id") Integer id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(postingService.getPostingById(id), HttpStatus.OK);
        }
    }

    @GetMapping("/list")
    @Transactional
    public ResponseEntity<Page<PostingDTO>> getPostings(@RequestParam(defaultValue = "0") Integer pageNo,
                                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                                        @RequestParam(defaultValue = "id") String sortBy) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(postingService.getPostings(pageNo, pageSize, sortBy),
                    HttpStatusCode.valueOf(HttpStatus.OK.value()));
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PostingDTO> createPosting(PostingDTO postingDTO) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(postingService.createPosting(postingDTO),
                    HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
        }
    }

    @Transactional
    @PostMapping("/request")
    public ResponseEntity<ItemRequestDTO> createItemRequest(@RequestBody ItemRequestDTO itemRequestDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(postingService.createItemRequest(itemRequestDTO),
                    HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
        }
    }

    @Transactional
    @GetMapping("/{id}/requests")
    public ResponseEntity<List<ItemRequestDTO>> listRequestsForItem(@PathVariable("id") Integer postingId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(postingService.listRequestsForItem(postingId),
                    HttpStatusCode.valueOf(HttpStatus.OK.value()));
        }
    }

    @Transactional
    @GetMapping("/requests/lender/list")
    public ResponseEntity<Page<ItemRequestDTO>> listRequestsForItem_Lender(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(defaultValue = "postingId") String sortBy) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(postingService.listLenderRequests(pageNo, pageSize, sortBy),
                    HttpStatusCode.valueOf(HttpStatus.OK.value()));
        }
    }

    @Transactional
    @GetMapping("/requests/borrower/list")
    public ResponseEntity<Page<ItemRequestDTO>> listRequestsForItem_Borrower(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(defaultValue = "postingId") String sortBy) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(postingService.listBorrowerRequests(pageNo, pageSize, sortBy),
                    HttpStatusCode.valueOf(HttpStatus.OK.value()));
        }
    }

    @Transactional
    @GetMapping("/requests/review/{id}/accept")
    public ResponseEntity<ItemRequestDTO> acceptRequest(@PathVariable("id") Integer itemRequestId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(postingService.acceptRequest(itemRequestId),
                    HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
        }
    }

    @Transactional
    @GetMapping("/requests/review/{id}/reject")
    public ResponseEntity<ItemRequestDTO> rejectRequest(@PathVariable("id") Integer itemRequestId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(postingService.rejectRequest(itemRequestId),
                    HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
        }
    }
}
