package com.lec.project.human_resources.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.project.human_resources.domain.Admin;
import com.lec.project.human_resources.domain.Attendance;
import com.lec.project.human_resources.domain.AttendanceEnum;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
	@Query("SELECT a FROM Attendance a WHERE a.employee = :admin AND a.status = :status")
	Optional<Attendance> findByIdWithAttendance(@Param("admin") Admin admin, @Param("status") AttendanceEnum attendanceEnum);
}