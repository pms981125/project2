package com.lec.project.human_resources.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.project.human_resources.domain.Admin;
import com.lec.project.human_resources.domain.WorkLog;

public interface WorkLogRepository extends JpaRepository<WorkLog, Integer> {
	@Query("SELECT w FROM WorkLog w WHERE w.employee = :admin AND FUNCTION('YEAR', w.workDate) = FUNCTION('YEAR', CURRENT_DATE) AND FUNCTION('MONTH', w.workDate) = FUNCTION('MONTH', CURRENT_DATE)")
	List<WorkLog> findAllById(@Param("admin") Admin admin);
}