package com.estate.hdragon.domain.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
public class AbstractEntity {

    @Id
    @JsonProperty
    private Long id;

    @CreatedDate
    @Column(name="created_date")
    private LocalDate createdDate;

    @LastModifiedDate
    @Column(name="modified_date")
    private LocalDate modifiedDate;

    public Long getId() {
        return id;
    }

    public String getFormattedCreateDate() {
        if (createdDate == null) {
            return "";
        }
        return createdDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractEntity other = (AbstractEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AbstractEntity [id=" + id + ", createDate=" + createdDate + ", modifiedDate=" + modifiedDate + "]";
    }

}
