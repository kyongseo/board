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
     */
    @GetMapping("/deleteform/{id}")
    public String deleteForm(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findBoardById(id));
        return "boards/delete";
    }

    @PostMapping("/deleteform/{id}")
    public String deleteForm(@PathVariable Long id,
                             @RequestParam String password,
                             @ModelAttribute Board board,
                             RedirectAttributes redirectAttributes) {
        Board deleteBoard = boardService.findBoardById(id);
        if (deleteBoard.getPassword().equals(password)) {
            boardService.deleteBoardById(id);
            redirectAttributes.addFlashAttribute("msg", "삭제 성공");
            return "redirect:/list";
        } else {
            redirectAttributes.addFlashAttribute("msg", "삭제 실패");
            return "redirect:/delete";
        }
    }

    /**
     * 게시글 수정 폼
     */
    @GetMapping("/updateform/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);

        model.addAttribute("board", boardService.findBoardById(id));
        return "boards/update";
    }

    @PostMapping("/updateform/{id}")
    public String editBoard(@ModelAttribute Board board,
                            @PathVariable Long id,
                            @RequestParam String password,
                            RedirectAttributes redirectAttributes) {
        Board updateBoard = boardService.findBoardById(id);

        if (updateBoard.getPassword().equals(password)){
            updateBoard.setName(board.getName());
            updateBoard.setTitle(board.getTitle());
            updateBoard.setContent(board.getContent());
            updateBoard.setCreatedAt(LocalDateTime.now());
            boardService.saveBoard(updateBoard);

            redirectAttributes.addFlashAttribute("msg", "수정성공");

            return "redirect:/list";
        }else {
            redirectAttributes.addFlashAttribute("msg", "수정 실패");
            return "redirect:/update/" + id;
        }
    }
}