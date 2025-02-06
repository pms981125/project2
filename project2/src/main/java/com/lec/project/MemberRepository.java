package com.lec.project;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, String> {
	@Query("SELECT m FROM Member m JOIN FETCH m.roleSet WHERE m.id = :id")
	Optional<Member> findByIdWithRoles(@Param("id") String id);

	@EntityGraph(attributePaths = "roleSet")
	Optional<Member> findByEmail(String email);
}