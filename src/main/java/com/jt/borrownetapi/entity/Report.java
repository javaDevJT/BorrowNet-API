package com.jt.borrownetapi.entity;

import com.jt.borrownetapi.model.enums.Reason;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User reportedUser;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Reason reason;
    @ManyToOne(optional = false)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User submitter;
    @Length(max = 500)
    @Column(nullable = true)
    private String details;
}
