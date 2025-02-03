package com.lec.project.shoppingmall.domain.cart.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lec.project.Member;
import com.lec.project.account.domain.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ordered extends BaseEntity {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Member member;
	
	@Column(length = 500)
	private String specialRequests;
	
	
	@Builder.Default
	private LocalDateTime orderDate = LocalDateTime.now();
	
	private int totalAmount;
	
	@Builder.Default
	@OneToMany(mappedBy = "ordered", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderedProduct> orderedProducts = new ArrayList<>();
}
