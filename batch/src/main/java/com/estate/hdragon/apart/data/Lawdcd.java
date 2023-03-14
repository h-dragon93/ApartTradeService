package com.estate.hdragon.apart.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class Lawdcd {
    @Id
    @GeneratedValue
    private Long lawd_cd_id;
    private String lawd_cd;
    private String sido_nm;
    private String sgg_nm;
}
