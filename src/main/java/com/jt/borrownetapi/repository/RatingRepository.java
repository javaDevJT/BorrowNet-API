package com.jt.borrownetapi.repository;

import com.jt.borrownetapi.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer>, PagingAndSortingRepository<Rating, Integer> {
    public Page<Rating> findAll(Pageable pageable);

    Page<Rating> findByRatedUser_Id(Integer id, Pageable pageable);
}
