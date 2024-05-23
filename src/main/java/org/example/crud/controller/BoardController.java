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

        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.ASC, "createdAt"));

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

    @GetMapping("/view")
    public String view(@RequestParam Long id, Model model) {
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
        redirectAttributes.addFlashAttribute("msg","게시글 등록 완료");
        return "redirect:/list";

    }

    /**
     * 게시글 삭제 폼
     * , @RequestParam String password
     */
    @GetMapping("/deleteform")
    public String deleteForm(@RequestParam Long id) {
            boardService.deleteBoardById(id);
        return "redirect:/list";
    }

    /**
     * 게시글 수정 폼
     */
    @GetMapping("/updateform")
    public String updateForm(@RequestParam Long id, Model model) {
        model.addAttribute("board", boardService.findBoardById(id));
        return "boards/update";
    }

    @PostMapping("/updateform")
    public String editBoard(@ModelAttribute("board") Board board) {
        boardService.saveBoard(board);

        return "redirect:/list";
    }
}
