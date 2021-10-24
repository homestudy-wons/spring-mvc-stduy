package com.example.dto;

import com.example.model.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String status;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.status = entity.getStatus();
    }


    public Posts toEntity(){
        return Posts.builder()
                .title(this.title)
                .content(this.content)
                .author(this.author)
                .status(this.status)
                .build();
    }
}