package com.jt.borrownetapi.repository;

import com.jt.borrownetapi.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer>, PagingAndSortingRepository<Report, Integer> {
    public Page<Report> findAll(Pageable pageable);
}
