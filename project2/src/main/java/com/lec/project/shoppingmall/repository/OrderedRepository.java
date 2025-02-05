package com.lec.project.shoppingmall.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.project.Member;
import com.lec.project.shoppingmall.domain.cart.order.Ordered;

public interface OrderedRepository extends JpaRepository<Ordered, Long> {
	@Query("SELECT o FROM Ordered o WHERE o.member = :member ORDER BY o.orderDate DESC")
    Page<Ordered> findByMemberOrderByOrderDateDesc(@Param("member") Member member, Pageable pageable);
    
    @Query("SELECT o FROM Ordered o WHERE o.member = :member ORDER BY o.orderDate DESC")
    List<Ordered> findByMemberOrderByOrderDateDesc(@Param("member") Member member);
    
    // 주문 상태별 조회
    @Query("SELECT o FROM Ordered o WHERE o.status= :status ORDERED BY o.orderDate DESC")
    List<Ordered> findByStatus(@Param("status") String status);
    
    // 날짜 범위로 주문 조회
    @Query("SELECT o FROM Ordered o WHERE o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<Ordered> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // 복합 검색
	@Query("SELECT o FROM Ordered o WHERE " +
			"(:status IS NULL OR o.status = :status) AND " +
			"(:startDate IS NULL OR o.orderDate >= :startDate) AND " +
			"(:endDate IS NULL OR o.orderDate <= :endDate) AND " +
			"(:searchTerm IS NULL OR " +
			"LOWER(o.member.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
			"LOWER(CAST(o.id AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
	 Page<Ordered> searchOrders(
		 @Param("status") String status,
		 @Param("startDate") LocalDateTime startDate,
		 @Param("endDate") LocalDateTime endDate,
		 @Param("searchTerm") String searchTerm,
		 Pageable pageable
	 );
    
}