package com.lec.project.shoppingmall.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.project.Member;
import com.lec.project.shoppingmall.domain.cart.order.Ordered;

public interface OrderedRepository extends JpaRepository<Ordered, Long> {
	@Query("SELECT o FROM Ordered o WHERE o.member = :member ORDER BY o.order_date DESC")
    Page<Ordered> findByMemberOrderByOrder_dateDesc(@Param("member") Member member, Pageable pageable);
    
    @Query("SELECT o FROM Ordered o WHERE o.member = :member ORDER BY o.order_date DESC")
    List<Ordered> findByMemberOrderByOrder_dateDesc(@Param("member") Member member);
}