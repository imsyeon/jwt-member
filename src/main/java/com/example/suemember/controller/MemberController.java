package com.example.suemember.controller;

import com.example.suemember.domain.entity.Member;
import com.example.suemember.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

        Member loginMember = memberService.loginMember(member.getEmail(), member.getPassword());

        // 세션
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("loginMember", loginMember);


        return ResponseEntity.status(HttpStatus.OK).body(loginMember);
    }

    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (httpSession != null) {
            httpSession.invalidate();
        }

        return ResponseEntity.status(HttpStatus.OK).body("logout");
    }

    //Test-Header값 임의로 지정해서 로그아웃
    @GetMapping("/logout2")
    public ResponseEntity logout2(@RequestHeader("Test-Header") String token, HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (token.equals("ok") && httpSession != null) {

            httpSession.invalidate();

            return ResponseEntity.status(HttpStatus.OK).body("logout");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail");
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity updateMemeber(@RequestHeader("Test-Header") String token,
                                        @RequestBody Member member, @PathVariable("id") Long id,
                                        HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (token.equals("ok") && httpSession != null) {

            memberService.updateMember(id, member);
            httpSession.invalidate();

            return ResponseEntity.status(HttpStatus.OK).body("update succeed");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("update fail");
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMemer(@RequestHeader("Test-Header") String token,
                                      @PathVariable("id") Long id,
                                      HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (token.equals("ok") && httpSession != null) {

            memberService.deleteMember(id);
            httpSession.invalidate();

            return ResponseEntity.status(HttpStatus.OK).body("bye bye!");

        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 탈퇴 실패");
    }
}