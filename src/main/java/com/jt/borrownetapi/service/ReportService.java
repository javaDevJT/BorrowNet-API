package com.jt.borrownetapi.service;

import com.jt.borrownetapi.dto.PublicUserDTO;
import com.jt.borrownetapi.dto.ReportDTO;
import com.jt.borrownetapi.entity.Report;
import com.jt.borrownetapi.entity.User;
import com.jt.borrownetapi.repository.ReportRepository;
import com.jt.borrownetapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ReportService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    public PublicUserDTO reportUser(Integer targetId, ReportDTO reportDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        Report report = fromReportDTO(reportDTO);
        Optional<User> targetUserOptional = userRepository.findById(targetId);
        if (targetUserOptional.isPresent()) {
            report.setSubmitter(userByEmail);
            report.setReportedUser(targetUserOptional.get());
            report.getReportedUser().addReportReceivedToProfile(report);
            report.getSubmitter().addReportWrittenToProfile(report);
            report = reportRepository.save(report);
            return PublicUserDTO.fromUser(report.getReportedUser());
        } else {
            throw new EntityNotFoundException("The target user for report does not exist.");
        }
    }

    public Report fromReportDTO(ReportDTO reportDTO) {
        return Report.builder().id(reportDTO.getId())
                .reportedUser(reportDTO.getReportedUser() != null ?
                        userRepository.findById(reportDTO.getReportedUser()).get() :
                        null)
                .reason(reportDTO.getReason())
                .submitter(reportDTO.getSubmitter() != null ?
                        userRepository.findById(reportDTO.getSubmitter()).get() :
                        null)
                .details(reportDTO.getDetails())
                .build();
    }
}
