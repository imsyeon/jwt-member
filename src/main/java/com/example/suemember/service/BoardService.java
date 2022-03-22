package com.example.suemember.service;

import com.example.suemember.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {

    Page<Board> getBoardList(Pageable pageable, String searchKeyword);

    Board insertBoard(Board board);

    Board getBoard(Long seq);

    Board updateBoard(Long seq, Board board);

    void deleteBoard(Long seq);

}
