package com.lec.project.shoppingmall.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResponseDTO<E> {

	private int page;
	private int size;
	private int total;

	private int start; // 시작페이지번호
	private int end; // 끝 페이지번호

	private boolean prev; // 이전페이지 존재 여부
	private boolean next; // 다음페이지 존재 여부

	private List<E> dtoList;

	@Builder(builderMethodName = "withAll")
	public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {
		
		this.page = pageRequestDTO.getPage();
		this.size = pageRequestDTO.getSize();
		this.total= total;
		this.dtoList = dtoList != null ? dtoList : List.of();

		if (total > 0) {
	        this.end = (int) (Math.ceil(this.page / 10.0)) * 10;
	        this.start = this.end - 9;
	        int last = (int)(Math.ceil((total / (double)size)));
	        this.end = end > last ? last : end;
	        this.prev = this.start > 1;
	        this.next = total > this.end * this.size;
	    } else {
	        this.start = 1;
	        this.end = 1;
	        this.prev = false;
	        this.next = false;
	    }
	}
}