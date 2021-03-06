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
import java.util.Optional;
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

    public String join(Member member) {
        validateDuplicateMember(member);
        return member.getId();
    }

    @Transactional
    public String withdraw(String id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + id));

        Posts posts = postsRepository.findByAuthor(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원의 게시물이 없습니다. id=" + id));

        if(member.getId().equals(posts.getAuthor())){
            posts.deletePosts();
        }

        member.withdraw();

        return "withdraw";
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

    private void validateDuplicateMember(Member member){
        Optional<Member> findMembers = memberRepository.findById(member.getId());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }
}
