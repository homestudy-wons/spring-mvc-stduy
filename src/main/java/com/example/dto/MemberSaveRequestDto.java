package com.example.dto;

import com.example.model.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberSaveRequestDto {

    @NotNull
    private String id;
    private String password;
    private String name;

    @Builder
    public MemberSaveRequestDto(String id, String password, String name){
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .password(password)
                .name(name)
                .build();
    }
}
