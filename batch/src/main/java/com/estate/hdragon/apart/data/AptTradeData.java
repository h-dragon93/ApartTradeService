package com.estate.hdragon.apart.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

//@Data
//@ToString
//@AllArgsConstructor

@Data
@Entity(name = "apttradedata")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AptTradeData {


    //@GenericGenerator(name= "uuid2", strategy = "uuid")
   //@GeneratedValue(generator = "uuids")
    @Id
    @GeneratedValue
    private int id;
    private String apartName;
    private String cancleDealDay;
    private int year;
    private String dealerAddress;
    @Column(name="dong_name")
    private String dongName;
    private String dealAmount;
    private String exclusiveAreaUse;
    private String cancelYn;
    private int buildYear;
    private int month;
    private String reqGbn;
    private int lawdCd;
    private String jibun;
    private int floor;
    private int day;
}
