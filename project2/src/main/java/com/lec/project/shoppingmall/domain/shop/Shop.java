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
    private String product_code;

	@Column(nullable = false)
	private String board_title;

	@Column//detail1
	private String board_content1;

	@Column//detail2
	private String board_content2;

	// 이후 상품 묶음 판매등의 확장을 위해 OneToOne가 아닌 ManyToOne으로 코드 작성
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_code", referencedColumnName = "product_code", insertable = false, updatable = false)
	private Product product;
	
	public void changeProductCode(String product_code) {
		this.product_code = product_code;
	}
	
	public void changeTitle(String board_title) {
		this.board_title = board_title;
	}
	
	public void changeDetail1(String board_contetn1) {
		this.board_content1 = board_contetn1;
	}
	
	public void changeDetail2(String board_contetn2) {
		this.board_content2 = board_contetn2;
	}
}
