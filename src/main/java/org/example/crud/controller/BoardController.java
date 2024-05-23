package org.example.crud.controller;

import lombok.RequiredArgsConstructor;
import org.example.crud.domain.Board;
import org.example.crud.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/list")
public class BoardController {

    private final BoardService boardService;


    /**
     * 게시글 목록 보기
     */
    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Board> boards = boardService.findAllBoards(pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);

        return "boards/list";
    }

    /**
     * 게시글 상세 조회 페이지
     * /localhost/view?id=13&page=1 같은 형식일려면 @RequestParam 사용
     */
//    @GetMapping("/view/{id}")
//    public String view(@PathVariable Long id, Model model) {
//        Board board = boardService.findBoardById(id);
//        model.addAttribute("board", board);
//
//        return "boards/view";
//    }
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);

        return "boards/view";
    }

    /**
     * 게시글 등록 폼
     */
    @GetMapping("/writeform")
    public String writeBoard(Model model) {
        model.addAttribute("board", new Board());
        return "boards/write";
    }

    @PostMapping("/writeform")
    public String writeBoard(@ModelAttribute("board") Board board, RedirectAttributes redirectAttributes) {
        board.setCreatedAt(LocalDateTime.now());
        boardService.saveBoard(board);
        redirectAttributes.addFlashAttribute("msg", "게시글 등록 완료");
        return "redirect:/list";

    }

    /**
     * 게시글 삭제 폼
     * , @RequestParam String password
     */
//    @GetMapping("/deleteform/{id}")
//    public String deleteForm(@PathVariable Long id) {
//        boardService.deleteBoardById(id);
//        return "boards/delete";
//    }
    @GetMapping("/deleteform/{id}")
    public String deleteForm(@PathVariable Long id, Model model) {
        model.addAttribute("board", new Board());
        return "boards/delete";
    }

//    @PostMapping("/deleteform/{id}")
//    public String deleteForm(@PathVariable Long id, @ModelAttribute("board") Board board, @RequestParam String password) {
//        Board existingBoard = boardService.findBoardById(id);
//        if (existingBoard != null && existingBoard.getPassword().equals(password)) {
//            boardService.deleteBoardById(id);
//            return "redirect:/list";
//        } else {
//            // 비밀번호가 일치하지 않을 경우 처리
//            return "redirect:/list"; // 혹은 다른 처리 방법을 선택할 수 있습니다.
//        }
//    }

    @PostMapping("/deleteform/{id}")
    public String deleteForm(@PathVariable Long id, @RequestParam String password) {
        Board existingBoard = boardService.findBoardById(id);
        if (existingBoard != null && existingBoard.getPassword().equals(password)) {
            boardService.deleteBoardById(id);
            return "redirect:/list";
        } else {
            // 비밀번호가 일치하지 않을 경우 처리
            return "redirect:/list"; // 혹은 다른 처리 방법을 선택할 수 있습니다.
        }
    }


    /**
     * 게시글 수정 폼
     */
//    @GetMapping("/updateform/{id}")
//    public String updateForm(@PathVariable Long id, Model model, @RequestParam String password) {
//        Board board = boardService.findBoardById(id);
//        if (board != null && board.getPassword().equals(password)) {
//            model.addAttribute("board", board);
//            return "boards/update";
//        }else {
//            return "redirect:/list";
//        }
//    }

    @GetMapping("/updateform/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);

        model.addAttribute("board", board);
        return "boards/update";
    }

//    @PostMapping("/updateform")
//    public String editBoard(@ModelAttribute("board") Board board) {
//        boardService.saveBoard(board);
//
//        return "redirect:/list";
//    }

//    @PostMapping("/updateform/{id}")
//    public String editBoard(@ModelAttribute("board") Board board, @RequestParam String password) {
//        Board existingBoard = boardService.findBoardById(board.getId());
//        if (existingBoard != null && existingBoard.getPassword().equals(password)) {
//            existingBoard.setName(board.getName());
//            existingBoard.setTitle(board.getTitle());
//            existingBoard.setContent(board.getContent());
//            // 다른 필드도 필요에 따라 업데이트
//
//            boardService.saveBoard(existingBoard);
//        }
//        return "redirect:/list";
//    }

    @PostMapping("/updateform/{id}")
    public String editBoard(@ModelAttribute("board") Board board) {
        Board existingBoard = boardService.findBoardById(board.getId());
            existingBoard.setName(board.getName());
            existingBoard.setTitle(board.getTitle());
            existingBoard.setContent(board.getContent());
            existingBoard.setCreatedAt(LocalDateTime.now());

            boardService.saveBoard(existingBoard);
        return "redirect:/list";
    }
}