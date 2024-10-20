package com.jt.borrownetapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jt.borrownetapi.entity.Report;
import com.jt.borrownetapi.model.enums.Reason;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PublicUserDTO reportedUser;
    @NotNull
    private Reason reason;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PublicUserDTO submitter;
    @NotNull
    @Length(max = 500)
    private String details;

    public static ReportDTO fromReport(Report report) {
        return ReportDTO.builder().id(report.getId())
                .reportedUser(PublicUserDTO.fromUser(report.getReportedUser()))
                .reason(report.getReason())
                .submitter(PublicUserDTO.fromUser(report.getSubmitter()))
                .details(report.getDetails())
                .build();
    }
}
