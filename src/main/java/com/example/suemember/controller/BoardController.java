package com.example.suemember.controller;

import com.example.suemember.domain.entity.Board;
import com.example.suemember.service.BoardService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@NoArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {
    @Autowired
    private BoardService boardService;

    // view
    @GetMapping("/index")
    public String index() {
        return "/index";
    }

    // 게시판 리스트
    @ResponseBody
    @GetMapping("/list")
    public Page<Board> getBoardList(
            @PageableDefault(size = 5) Pageable pageable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "") String searchKeyword,
            @RequestParam(defaultValue = "seq") String sort,
            @RequestParam(defaultValue = "desc") String dir) {
        if (dir.equals("desc")) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, sort);
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, sort);
        }
        Page<Board> boardList = boardService.getBoardList(pageable, searchKeyword);
        return boardList;

    }
    // 게시글 등록
    @ResponseBody
    @PostMapping("/write")
    public ResponseEntity insertBoard(@RequestBody Board board) {
        Board write = boardService.insertBoard(board);

        return ResponseEntity.status(HttpStatus.CREATED).body(write);
    }

    // 상세 게시글
    @ResponseBody
    @GetMapping("/{seq}")
    public Board getBoard(@PathVariable("seq") Long seq) {

        return boardService.getBoard(seq);
    }

    // 게시글 수정
    @ResponseBody
    @PatchMapping("/{seq}")
    public Board updateBoard(@PathVariable("seq") Long seq, @RequestBody Board board) {


        return boardService.updateBoard(seq, board);
    }

    // 게시글 삭제
    @ResponseBody
    @DeleteMapping("/{seq}")
    public ResponseEntity deleteBoard(@PathVariable("seq") Long seq) {

        boardService.deleteBoard(seq);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
