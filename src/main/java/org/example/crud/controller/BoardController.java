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
    @GetMapping("/write")
    public String writeBoard(Model model) {
        model.addAttribute("board", new Board());
        return "boards/write";
    }

    @PostMapping("/write")
    public String writeBoard(@ModelAttribute("board") Board board, RedirectAttributes redirectAttributes) {
        board.setCreatedAt(LocalDateTime.now());
        boardService.saveBoard(board);
        redirectAttributes.addFlashAttribute("msg", "게시글 등록 완료");
        return "redirect:/list";

    }

    /**
     * 게시글 삭제 폼
     */
    @GetMapping("/delete/{id}")
    public String deleteForm(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findBoardById(id));
        return "boards/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteForm(@PathVariable Long id,
                             @RequestParam String password,
                             @ModelAttribute Board board,
                             Model model) {
        Board deleteBoard = boardService.findBoardById(id);
        if (deleteBoard.getPassword().equals(password)) {
            boardService.deleteBoardById(id);
            return "redirect:/list";
        } else {
            model.addAttribute("id", board.getId());
            model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("post", board); // post 객체도 모델에 추가
            return "boards/delete";
        }
    }

//    @PostMapping("/delete")
//    public String delete(@RequestParam(name="id") Long id,
//                         @RequestParam(name="password") String password,
//                         RedirectAttributes redirectAttributes) {
//        // @RequestParam으로 deleteform에서 hidden으로 name="id" 넘긴거 받아와서 id 변수에 주입,
//        // password도 name="password"로 받아와서 변수에 주입
//
//        if (boardService.findBoardById(id, password)) {
//            // password 확인 결과 true여야 삭제
//            boardService.deleteBoardById(id);
//            redirectAttributes.addFlashAttribute("message", "게시글이 정상적으로 삭제되었습니다.");
//            return "redirect:/list";
//        } else {    // password 불일치 시 삭제 폼으로 redirect
//            redirectAttributes.addFlashAttribute("message", "비밀번호가 일치하지 않습니다.");
//            return "redirect:/deleteform?id=" + id;
//        }
//    }

    /**
     * 게시글 수정 폼
     */
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);

        model.addAttribute("board", board);
        return "boards/update";
    }

    @PostMapping("/update/{id}")
    public String editBoard(@ModelAttribute Board board,
                            @PathVariable Long id,
                            @RequestParam String password,
                            Model model) {
        Board updateBoard = boardService.findBoardById(id);

        if (updateBoard.getPassword().equals(password)){
            updateBoard.setName(board.getName());
            updateBoard.setTitle(board.getTitle());
            updateBoard.setContent(board.getContent());
            updateBoard.setCreatedAt(LocalDateTime.now());
            boardService.saveBoard(updateBoard);
            return "redirect:/list";
        }else {
            model.addAttribute("id", board.getId());
            model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("post", board); // post 객체도 모델에 추가
            return "boards/update";
        }
    }
}