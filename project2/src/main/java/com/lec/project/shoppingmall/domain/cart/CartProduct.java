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
	@JoinColumn(name = "cart_id")
	private Cart cart;
	
	private String product_name;
	
	//물품 총 가격 합계
	private int total_price;
	
	// 물품 갯수
	private int count;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;
	
	public static CartProduct CreateProductCart(Cart cart, Product product
								, int total_count, int total_price) {
		
		CartProduct cartProduct = new CartProduct();
		cartProduct.setCart(cart);
		cartProduct.setProduct(product);
		cartProduct.setCount(total_count);
		cartProduct.setTotal_price(total_price);
		
		return cartProduct;
	}
	
    public void updateCount(int newCount) {
        this.count = newCount;
        this.total_price = newCount * this.product.getProduct_price();
    }
}
