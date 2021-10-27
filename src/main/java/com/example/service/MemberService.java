package com.example.service;

import com.example.dto.MemberSaveRequestDto;
import com.example.dto.MemberResponseDto;
import com.example.dto.MemberListResponseDto;
import com.example.model.Member;
import com.example.model.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public String save(MemberSaveRequestDto requestDto) {
        return memberRepository.save(requestDto.toEntity()).getId();
    }

    public MemberResponseDto findById (String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id="+ id));

        return new MemberResponseDto(member);
    }

    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(member -> new MemberResponseDto(member))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MemberListResponseDto> findAllDesc() {
        return memberRepository.findAllDesc().stream()
                .map(MemberListResponseDto::new)
                .collect(Collectors.toList());
    }
}
