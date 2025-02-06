package com.lec.project.shoppingmall.dto.magement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundProcessRequestDTO {
	private boolean approve;
	private String managerComment;
}
