package com.jt.borrownetapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "borrownet_user", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class User implements UserDetails {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column
    @NotNull
    private String firstName;
    @Column
    @NotNull
    private String lastName;
    @Column
    @NotNull
    private String email;
    @Column
    private Date date;
    @Column
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column
    @NotNull
    private String role = "ROLE_USER";
    @OneToOne
    private UserPreferences userPreferences;
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Posting> postings;
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratingsReceived;
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reportsReceived;
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratingsWritten;
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reportsWritten;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void addPosting(Posting posting) {
        postings.add(posting);
        posting.setLender(this);
    }

    public void removePosting(Posting posting) {
        postings.remove(posting);
        posting.setLender(null);
    }
    public void addRatingReceivedToProfile(Rating rating) {
        ratingsReceived.add(rating);
        rating.setRatedUser(this);
    }

    public void removeRatingReceivedFromProfile(Rating rating) {
        ratingsReceived.remove(rating);
        rating.setRatedUser(null);
    }
    public void addRatingWrittenToProfile(Rating rating) {
        ratingsWritten.add(rating);
        rating.setSubmitter(this);
    }

    public void removeRatingWrittenFromProfile(Rating rating) {
        ratingsWritten.remove(rating);
        rating.setSubmitter(null);
    }
    public void addReportReceivedToProfile(Report report) {
        reportsReceived.add(report);
        report.setReportedUser(this);
    }

    public void removeReportReceivedFromProfile(Report report) {
        reportsReceived.remove(report);
        report.setReportedUser(null);
    }
    public void addReportWrittenToProfile(Report report) {
        reportsWritten.add(report);
        report.setSubmitter(this);
    }

    public void removeReportWrittenFromProfile(Report report) {
        reportsWritten.remove(report);
        report.setSubmitter(null);
    }
}
