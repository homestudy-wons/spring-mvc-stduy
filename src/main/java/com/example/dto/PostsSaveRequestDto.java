package com.example.dto;

import com.example.model.Posts;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@ToString
public class PostsSaveRequestDto {

    @NotNull(message = "this title is net empty")
    private String title;

    @NotNull
    @Size(min=3, max=100, message = "content is greater than 3 characters and less than 100 characters")

    private String content;
    @NotNull(message = "author is not null")

    private String author;

    @Builder
    public PostsSaveRequestDto(String title, String content,
                               String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}

