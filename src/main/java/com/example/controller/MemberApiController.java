package com.example.controller;

import com.example.dto.MemberResponseDto;
import com.example.dto.MemberSaveRequestDto;
import com.example.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/member")
    public HashMap<String, Object> save(@Validated @RequestBody MemberSaveRequestDto requestDto) {
        memberService.save(requestDto);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("code", "so1");
        map.put("message", "member 저장에 성공하였습니다.");
        map.put("id", requestDto.getId());
        return map;
    }
    @DeleteMapping("api/v1/member/{id}")
    public String withdraw(@PathVariable String id) {

        return memberService.withdraw(id);
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
