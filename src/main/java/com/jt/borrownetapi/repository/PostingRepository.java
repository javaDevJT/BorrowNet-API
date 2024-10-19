package com.jt.borrownetapi.repository;

import com.jt.borrownetapi.entity.Posting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Integer>, PagingAndSortingRepository<Posting, Integer> {
    public Page<Posting> findAll(Pageable pageable);
}
