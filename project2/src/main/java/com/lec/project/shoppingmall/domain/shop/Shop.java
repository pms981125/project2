package com.lec.project.shoppingmall.domain.shop;

import com.lec.project.shoppingmall.domain.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bno;

	@Column
	private String productCode;

	@Column(nullable = false)
	private String boardTitle;

	@Column//firstDetail
	private String firstBoardContent;

	@Column//secondDetail
	private String secondBoardContent;

	// 이후 상품 묶음 판매등의 확장을 위해 OneToOne가 아닌 ManyToOne으로 코드 작성
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productCode", referencedColumnName = "productCode", insertable = false, updatable = false)
	private Product product;
	
	public void changeProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public void changeTitle(String boardTitle) {	
		this.boardTitle = boardTitle;
	}
	
	public void changeFirstDetail(String firstBoardContent) {
		this.firstBoardContent = firstBoardContent;
	}
	
	public void changeSecondDetail(String secondBoardContent) {
		this.secondBoardContent = secondBoardContent;
	}
}
