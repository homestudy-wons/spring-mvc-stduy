package com.example.service;

import com.example.dto.MemberSaveRequestDto;
import com.example.dto.MemberResponseDto;
import com.example.dto.PostsResponseDto;
import com.example.model.Member;
import com.example.model.MemberRepository;
import com.example.model.Posts;
import com.example.model.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PostsRepository postsRepository;

    @Transactional
    public String save(MemberSaveRequestDto requestDto) {
        return memberRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public String withdraw(String id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + id));
        member.withdraw();
        Posts posts = postsRepository.findByAuthor(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원의 게시물이 없습니다. id=" + id));
        if(posts.equals(memberRepository.findById(id))){
            posts.deletePosts();
        }

        return "withdraw";
    }

    // TODO: 21. 11. 8.
    // 회원 탈퇴시 게시글 삭제 로직 MemberService에서 분리하여 작성하기

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

}
