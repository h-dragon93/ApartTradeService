package com.estate.hdragon.domain.account;

import com.estate.hdragon.domain.common.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;


@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="account")
public class Account  {      //extends AbstractEntity

    @Column(nullable = false, unique = true)
    @JsonProperty
    @Id
    private Long id;

    @JsonProperty
    private String email;

    @JsonProperty
    private String nickname;

    @JsonProperty
    @CreatedDate
    @Column(name="created_date")
    private LocalDate createdDate;
}
