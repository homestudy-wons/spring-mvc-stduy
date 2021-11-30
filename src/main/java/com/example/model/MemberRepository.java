package com.example.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("SELECT m FROM Member m ORDER BY m.id DESC")
    List<Member> findAllDesc();
}
