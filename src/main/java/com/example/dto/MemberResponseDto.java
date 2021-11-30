package com.example.dto;

import com.example.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class MemberResponseDto {
    private String id;
    private String password;
    private String name;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.password = member.getPassword();
        this.name = member.getName();
    }

    public Member toEntity(){
        return Member.builder()
                .id(this.id)
                .password(this.password)
                .name(this.name)
                .build();
    }
}
