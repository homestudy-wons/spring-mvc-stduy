package com.example.dto;

import com.example.model.Member;
import lombok.Getter;

@Getter
public class MemberListResponseDto {
    private String id;
    private String password;
    private String name;

    public MemberListResponseDto(Member member) {
        this.id = member.getId();
        this.password = member.getPassword();
        this.name = member.getName();
    }
}
