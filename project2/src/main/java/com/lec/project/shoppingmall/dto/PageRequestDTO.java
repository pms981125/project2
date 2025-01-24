package com.lec.project.shoppingmall.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDTO {

	@Builder.Default
	private int page = 1;
	
	@Builder.Default // 한페이지에 10개
	private int size = 12;
	
	//private String type = "t"; // title
	
	private String keyword; // 검색어
	
	public Pageable getPageable(String...props) {
		return PageRequest.of(this.page-1, this.size, Sort.by(props).descending());
	}
	
	private String link;
	// localhost:8090/shop/list?page=1&size=10&keyword=???
	
	public String getLink() {
		
		if(link == null) {
			StringBuilder builder = new StringBuilder();
			
			builder.append("page= " + this.page);
			builder.append("size= " + this.size);
			
			if(keyword != null) {
				try {
					builder.append("&keyword=" + URLEncoder.encode(keyword, "utf-8"));
				} catch (UnsupportedEncodingException e) {
					//dummy
				}
			}
			link = builder.toString();
		}
		
		return link;
	}
}
