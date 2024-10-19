package com.jt.borrownetapi.entity;

import com.jt.borrownetapi.dto.PostingDTO;
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
public class Posting {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User lender;
    @Column(nullable = false)
    private String itemName;
    @Length(max = 500, message = "Item description max length is 500 characters.")
    @Column(nullable = true)
    private String itemDescription;
    @Column(columnDefinition = "VARCHAR(10485760)", nullable = false)
    private String itemPhoto;
    @Column(nullable = false)
    private int maxRentalHours;
    @Column(nullable = false)
    private boolean available;

    public static Posting fromPostingDTO(PostingDTO postingDTO) {
        return Posting.builder().id(postingDTO.getId())
                .itemName(postingDTO.getItemName())
                .itemDescription(postingDTO.getItemDescription())
                .itemPhoto(postingDTO.getItemPhoto())
                .maxRentalHours(postingDTO.getMaxRentalHours())
                .available(postingDTO.isAvailable())
                .build();
    }
}
