package com.example.dto;

import com.example.model.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    @NotNull
    private String title;
    private String content;
    private String author;
    private String status;

    @Builder
    public PostsSaveRequestDto(String title, String content,
                               String author, String status) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.status = status;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .status(status)
                .build();
    }
}


