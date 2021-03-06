package com.example.controller;

import com.example.dto.PostsResponseDto;
import com.example.dto.PostsSaveRequestDto;
import com.example.dto.PostsUpdateRequestDto;
import com.example.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostService postService;

    @PostMapping("/api/v1/posts")
    public Long save(@Validated @RequestBody PostsSaveRequestDto requestDto) {

        return postService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody
            PostsUpdateRequestDto requestDto) {

        return postService.update(id, requestDto);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public String delete(@PathVariable Long id) {

        return postService.delete(id);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById (@PathVariable Long id) {

        return postService.findById(id);
    }

    @GetMapping("/api/v1/posts")
    public List<PostsResponseDto> findAll (){
        return postService.findAll();
    }

}
