package com.estate.hdragon.apart.repository;

import com.estate.hdragon.apart.data.AptTradeData;
import com.estate.hdragon.apart.data.Lawdcd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LawdCdRepository extends JpaRepository<Lawdcd, Long> {

}
