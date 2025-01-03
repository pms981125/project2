package com.lec.project.regionboard.repository;

import com.lec.project.regionboard.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SecondHandMarketRepository extends JpaRepository<SecondHandMarket, Long> {
    // 특정 지역의 게시글만 조회
    List<SecondHandMarket> findByRegion(String region);
}