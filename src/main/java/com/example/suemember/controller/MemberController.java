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
@RequestMapping("/") //복수로 네이밍
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

    // responseEntity 사용 방법 확인
    public ResponseEntity<TokenResponse> login(@RequestBody Member member) {

        return ResponseEntity
                .ok()
                .body(memberService.loginMember(member.getEmail(), member.getPassword()));
    }


    //Test-Header값 임의로 지정해서 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Test-Header") String token, HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (token.equals("ok") && httpSession != null) {

            httpSession.invalidate();

            return ResponseEntity.status(HttpStatus.OK).body("logout");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail");
    }


    @PatchMapping("/members/{id}") // serivce에서 return
    public ResponseEntity<String> updateMemeber(@RequestBody Member member, @PathVariable("id") Long id) {
        System.out.println(id);
            // 회원 정보를 불러서 비교한 뒤에 유효하면 회원 수정이 되게끔 유효성 체크 ( 롤백, 트랜잭션, 세션이 유효하지 않을 시)
        return ResponseEntity
                .ok()
                .body(memberService.updateMember(id, member).toString());
    }

    // controller에서는 최소한의 처리만 함 session이나 토큰은 service 단으로 다 넘어가게 해서 service에서 예외처리
    @DeleteMapping("/members/{id}")
    public ResponseEntity<String> deleteMember(@RequestHeader("Test-Header") String token,
                                              @PathVariable("id") Long id,
                                              HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (token.equals("ok") && httpSession != null) {
            // id 정책이 맞는지, id가 있는지 우선 확인 후에 삭제 진행  -> 중복 작업이 생기면 따로 service로 빼서 구현
            memberService.deleteMember(id);
            httpSession.invalidate();

            // 지운 결과 return 받아서 확인 받기
            return ResponseEntity.status(HttpStatus.OK).body("bye bye!");

        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 탈퇴 실패");
    }
}