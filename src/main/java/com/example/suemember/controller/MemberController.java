package com.example.suemember.controller;

import com.example.suemember.domain.entity.Member;
import com.example.suemember.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 모든 회원 정보
    @GetMapping("/all")
    public List<Member> getAllMember() {

        return memberService.getAllMember() ;
    }

    // 회원가입
    @PostMapping("/add")
    public Member addNewMember (@RequestBody Member member) {

            member = memberService.addNewMember(member);

        return member;
    }


}
