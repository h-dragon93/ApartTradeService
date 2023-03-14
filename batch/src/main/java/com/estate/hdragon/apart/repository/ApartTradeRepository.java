package com.estate.hdragon.apart.repository;

import com.estate.hdragon.apart.data.AptTradeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface ApartTradeRepository extends JpaRepository<AptTradeData, UUID> {
}
