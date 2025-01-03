package com.lec.project.account.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
@EntityListeners(value = { AuditingEntityListener.class})
public class BaseEntity {
	@CreatedDate
	@Column(name = "createDate", updatable = false)
	private LocalDateTime createDate;
}
