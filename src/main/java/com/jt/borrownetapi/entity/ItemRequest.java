package com.jt.borrownetapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Posting posting;
    @ManyToOne(optional = false)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User requester;
    @Column(nullable = false)
    private Integer itemRequestLength;
    @Column(nullable = false)
    private Boolean requestReviewed = false;
    @Column(nullable = false)
    private Boolean requestAccepted = false;
}
