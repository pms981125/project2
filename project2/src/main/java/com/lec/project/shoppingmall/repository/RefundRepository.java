package com.lec.project.shoppingmall.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.project.shoppingmall.domain.cart.order.OrderStatus;
import com.lec.project.shoppingmall.domain.refund.Refund;

public interface RefundRepository extends JpaRepository<Refund, Long>{
	
    Optional<Refund> findByOrderId(Long orderId);

    @Query("SELECT r FROM Refund r WHERE " +
           "(:status IS NULL OR r.currentStatus = :status) AND " +
           "(:startDate IS NULL OR r.requestDate >= :startDate) AND " +
           "(:endDate IS NULL OR r.requestDate <= :endDate) AND " +
           "(:search IS NULL OR " +
           "LOWER(r.order.customerName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(CAST(r.order.id AS string)) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Refund> searchRefunds(
        @Param("status") OrderStatus status,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("search") String search,
        Pageable pageable
    );
    
    @Query("SELECT r FROM Refund r WHERE r.order.member.id = :memberId ORDER BY r.requestDate DESC")
    Page<Refund> findByOrderMemberIdOrderByRequestDateDesc(@Param("memberId") String memberId, Pageable pageable);
    
    List<Refund> findByCurrentStatus(OrderStatus status);
    
}
