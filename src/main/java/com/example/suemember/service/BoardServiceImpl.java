package com.example.suemember.service;

import com.example.suemember.domain.entity.Board;
import com.example.suemember.domain.repository.BoardRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardRepository boardRepo;

    @Override
    public Page<Board> getBoardList(Pageable pageable, String searchKeyword) {
        Page<Board> getBoardList = boardRepo.findByTitleContaining(searchKeyword, pageable);
        if (searchKeyword.isEmpty()) {
            getBoardList = boardRepo.findAll(pageable);
        }
        return getBoardList;
    }

    @Override
    public Board insertBoard(Board board) {

        return boardRepo.save(board);
    }

    @Override
    public Board getBoard(Long seq) {

        return boardRepo.findById(seq).get();
    }

    @Override
    public Board updateBoard(Long seq, Board board) {
        board.setSeq(seq);
        return boardRepo.save(board);
    }

    @Override
    public void deleteBoard(Long seq) {

        boardRepo.deleteById(seq);
    }
}

