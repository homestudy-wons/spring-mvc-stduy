package com.example.controller;

import com.example.dto.MemberResponseDto;
import com.example.dto.MemberSaveRequestDto;
import com.example.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/member")
    public String save(@Validated @RequestBody MemberSaveRequestDto requestDto) {

        return memberService.save(requestDto);
    }

    @GetMapping("/api/v1/member/{id}")
    public MemberResponseDto findById(@PathVariable String id) {
        return memberService.findById(id);
    }

    @GetMapping("/api/v1/member")
    public List<MemberResponseDto> findAll() {
        return memberService.findAll();
    }
}
