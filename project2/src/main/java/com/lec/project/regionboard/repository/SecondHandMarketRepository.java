package com.lec.project.regionboard.repository;

import com.lec.project.regionboard.model.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SecondHandMarketRepository extends JpaRepository<SecondHandMarket, Long> {
    // 특정 지역의 게시글만 조회 (페이징 처리)
    Page<SecondHandMarket> findByRegion(String region, Pageable pageable);

    // 특정 지역과 제목으로 검색 (페이징 처리)
    public Page<SecondHandMarket> findByRegionAndTitleContaining(String region, String title, Pageable pageable);

}
