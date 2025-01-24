package com.lec.project.regionboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lec.project.Member;
import com.lec.project.regionboard.model.MemberRegion;

public interface MemberRegionRepository extends JpaRepository<MemberRegion, Long> {
    // 로그인한 유저의 지역 정보 찾기
    MemberRegion findByMember(Member member);
}

