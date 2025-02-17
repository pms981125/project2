package com.lec.project.shoppingmall.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.project.Member;
import com.lec.project.shoppingmall.domain.cart.Cart;


public interface CartRepository extends JpaRepository<Cart, Long>{
	Optional<Cart> findByMemberId(String memberId);

	@Query("SELECT c FROM Cart c WHERE c.member = :member")
	List<Cart> findAllByMember(@Param("member") Member member);
}
