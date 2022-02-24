package com.example.suemember.controller;

import com.example.suemember.domain.entity.Member;
import com.example.suemember.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

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

        return memberService.getAllMember();
    }

    // 회원가입
    @PostMapping("/add")
    public Member addNewMember(@RequestBody Member member) {

        member = memberService.addNewMember(member);

        return member;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Member member, HttpServletRequest request) {

        System.out.println("login controller");

        Member loginMember = memberService.loginMember(member.getEmail(), member.getPassword());

        // 세션
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("loginMember",loginMember);


        return ResponseEntity.status(HttpStatus.OK).body(loginMember);
    }

    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (httpSession!= null) {
            httpSession.invalidate();
        }


        return ResponseEntity.status(HttpStatus.OK).body("logout");
    }


}
