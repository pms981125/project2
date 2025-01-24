package com.lec.project.accountHistory.dto;

import java.util.List;

import lombok.Builder;

public class PageResponseDTO<E> {
	public int page;
	public int size;
	public int total;
	
	public int start;
	public int end;
	
	public boolean prev;
	public boolean next;
	
	public List<E> dtoList;
	
	@Builder(builderMethodName = "withAll")
	public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {
		if(total <= 0) {
			return;
		}
		
		this.page = pageRequestDTO.getPage();
		this.size = pageRequestDTO.getSize();
		
		this.total = total;
		this.dtoList = dtoList;
		
		this.end = (int)(Math.ceil(this.page / 10.0)) * 10;
		this.start = this.end - 9;
		int last = (int)(Math.ceil((total / (double) size)));
		this.end = end > last ? last : end;
		this.prev = this.start > 1;
		this.next = total > this.end * this.size;
	}
}
