package com.lec.project.shoppingmall.domain.cart;

import com.lec.project.shoppingmall.domain.product.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "cartId")
	private Cart cart;
	
	private String productName;
	
	//물품 총 가격 합계
	private int totalPrice;
	
	// 물품 갯수
	private int count;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;
	
	public static CartProduct CreateProductCart(Cart cart, Product product,
			int totalCount, int totalPrice) {
		
		CartProduct cartProduct = new CartProduct();
		cartProduct.setCart(cart);
		cartProduct.setProduct(product);
		cartProduct.setCount(totalCount);
		cartProduct.setTotalPrice(totalPrice);
		
		return cartProduct;
	}
	
    public void updateCount(int newCount) {
        this.count = newCount;
        this.totalPrice = newCount * this.product.getProductPrice();
    }
}
