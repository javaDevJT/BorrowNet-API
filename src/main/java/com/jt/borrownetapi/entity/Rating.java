package com.jt.borrownetapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class Rating {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User ratedUser;
    @Max(5)
    @Min(1)
    @Column(nullable = false)
    private Integer rating;
    @ManyToOne(optional = false)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User submitter;
    @Length(max = 500)
    @Column(nullable = true)
    private String details;
}
