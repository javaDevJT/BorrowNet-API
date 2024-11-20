package com.jt.borrownetapi.repository;

import com.jt.borrownetapi.entity.ItemRequest;
import com.jt.borrownetapi.entity.Posting;
import com.jt.borrownetapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer>, PagingAndSortingRepository<ItemRequest, Integer> {

    Optional<ItemRequest> findByRequesterAndPostingAndRequestReviewed(User requester, Posting posting, Boolean requestReviewed);

    List<ItemRequest> findByPosting_Id(Integer id);

    Page<ItemRequest> findByPosting_Lender(User lender, Pageable pageable);

    Page<ItemRequest> findByRequester(User borrower, Pageable pageable);
}