package com.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsDeleteRequestDto {
    private String status;

    @Builder
    public PostsDeleteRequestDto(String status) {
        this.status = status;
    }
}

