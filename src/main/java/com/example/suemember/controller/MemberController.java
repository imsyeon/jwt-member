package com.example.suemember.controller;

import com.example.suemember.domain.entity.Member;
import com.example.suemember.dto.TokenResponse;
import com.example.suemember.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {

        this.memberService = memberService;

    }

    // 모든 회원 정보
    @GetMapping("/members")
    public List<Member> getAllMember() {

        return memberService.getAllMember();
    }

    // 회원가입
    @PostMapping("/member")
    public ResponseEntity addNewMember(@RequestBody Member member) {

        return ResponseEntity
                .ok()
                .body(memberService.addNewMember(member));
    }

    @PostMapping("/member/test")
    public Map userResponseTest() {
        Map<String, String> result = new HashMap<>();
        result.put("result","success");
        return result;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody Member member) {

        return ResponseEntity
                .ok()
                .body(memberService.loginMember(member.getEmail(), member.getPassword()));
    }

    @GetMapping("/logout/{id}")
    public ResponseEntity<TokenResponse> logout(@PathVariable("id") Long id, @RequestHeader("REFRESH_TOKEN") String refreshToken) {

        return ResponseEntity
                .ok()
                .body(memberService.logoutMember(id, refreshToken));
    }

    @PatchMapping("/members/{id}")
    public ResponseEntity<TokenResponse> updateMemeber(@RequestBody Member member, @PathVariable("id") Long id) {
        System.out.println("updateMemeber "+id);

            // 회원 정보를 불러서 비교한 뒤에 유효하면 회원 수정이 되게끔 유효성 체크 ( 롤백, 트랜잭션, 세션이 유효하지 않을 시)
        return ResponseEntity
                .ok()
                .body(memberService.updateMember(id, member));
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable("id") Long id, @RequestHeader("REFRESH_TOKEN")String refreshToken) {

            return ResponseEntity
                    .ok()
                    .body(memberService.deleteMember(id, refreshToken));
    }
}