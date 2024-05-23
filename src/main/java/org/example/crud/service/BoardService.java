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

    // 목록 보여주기
    @Transactional(readOnly = true)
    public Iterable<Board> findAll() {
        return boardRepository.findAll();
    }

    // 상세정보
    @Transactional(readOnly = true)
    public Board findBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    // 게시글 등록
    //Spring Data 에서 제공하는 save라는 메서드는 id에 해당하는 값이 이미 존재한다면 수정
    //없다면 생성한다.
    @Transactional
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    //게시글 삭제
    @Transactional
    public void deleteBoardById(Long id) {
        boardRepository.deleteById(id);
    }

    /**
     * 페이진 처리된 글 목록 조회
     */
    public Page<Board> findAllBoards(Pageable pageable) {
//        Pageable sortedByDescId = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),
//                Sort.by(Sort.Direction.ASC, "id"));
        return boardRepository.findAll(pageable);
    }

    /**
     * 게시글 수정
     * 비밀번호 확인 후 수정 가능하도록 구현
     */
//    @Transactional
//    public Board updateBoard(Board board, String password) {
//        // 게시글 ID로 저장된 비밀번호 확인
//        Board existingBoard = boardRepository.findById(board.getId()).orElse(null);
//        if (existingBoard != null && existingBoard.getPassword().equals(password)) {
//            // 비밀번호가 일치하면 수정
//            existingBoard.setName(board.getName());
//            existingBoard.setTitle(board.getTitle());
//            existingBoard.setContent(board.getContent());
//            // 다른 필드도 필요에 따라 수정
//
//            return boardRepository.save(existingBoard);
//        }
//        return null; // 비밀번호가 일치하지 않는 경우 null 반환
//    }
}