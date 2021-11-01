package com.example.controller;


import com.example.dto.MemberResponseDto;
import com.example.dto.PostsResponseDto;
import com.example.service.MemberService;
import com.example.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.HashMap;

@Controller
public class HelloWorldController {

    private final PostService postService;
    private final MemberService memberService;

    public HelloWorldController(PostService postService, MemberService memberService) {
        this.postService = postService;
        this.memberService = memberService;
    }

    @GetMapping("/hello2")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("posts", postService.findAllDesc());
        return "index";
    }

    @GetMapping("/posts/save")
    public String postSave(){ return "posts-save"; }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){

        PostsResponseDto dto = postService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }

    @GetMapping("/member/save")
    public String memberSave(){
        return "member-save";
    }

    @GetMapping("/message")
    @ResponseBody
    public HashMap<String, Object> retrunMessage() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("code", "so1");
        map.put("message", "member 저장에 성공하였습니다.");

        return map;
    }

    // TODO: 21. 11. 1. member 가입 성공 시 json 리턴 컨트롤러 호출하기 
}
