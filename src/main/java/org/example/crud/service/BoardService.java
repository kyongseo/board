package org.example.crud.service;

import lombok.RequiredArgsConstructor;
import org.example.crud.domain.Board;
import org.example.crud.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 상세정보
    @Transactional(readOnly = true)
    public Board findBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    // 게시글 등록
    @Transactional
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    //게시글 삭제
    @Transactional
    public void deleteBoardById(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<Board> findAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }
}