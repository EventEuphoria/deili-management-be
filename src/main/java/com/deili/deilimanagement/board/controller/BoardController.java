package com.deili.deilimanagement.board.controller;

import com.deili.deilimanagement.board.dto.BoardDto;
import com.deili.deilimanagement.board.dto.BoardRequestDto;
import com.deili.deilimanagement.board.entity.enums.BoardRole;
import com.deili.deilimanagement.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @MutationMapping
    public BoardDto createBoard(@Argument("boardRequestDTO") BoardRequestDto boardRequestDto){
        return boardService.createBoard(boardRequestDto);
    }

    @MutationMapping
    public BoardDto updateBoard(@Argument Long id, @Argument("boardRequestDTO") BoardRequestDto boardRequestDto){
        return boardService.updateBoard(id, boardRequestDto);
    }

    @MutationMapping
    public Boolean deleteBoard(@Argument Long id){
        boardService.deleteBoard(id);
        return true;
    }

    @QueryMapping
    public List<BoardDto> getAllBoards(){
        return boardService.getAllBoards();
    }

    @QueryMapping
    public BoardDto getBoardById(@Argument Long id){
        return boardService.getBoardById(id);
    }

    @MutationMapping
    public String inviteUserToBoard(@Argument Long boardId, @Argument Long userId, @Argument BoardRole role) {
        try {
            boardService.inviteUserToBoard(boardId, userId, role);
            return "User invited successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error inviting user to the board: " + e.getMessage());
        }
    }

    @MutationMapping
    public String updateBoardRole(@Argument Long boardId, @Argument Long userId, @Argument BoardRole role) {
        try {
            boardService.updateBoardRole(boardId, userId, role);
            return "Board role updated successfully"; // Return a success message
        } catch (Exception e) {
            throw new RuntimeException("Error updating board role: " + e.getMessage());
        }
    }

    @MutationMapping
    public String removeBoardAssignee(@Argument Long boardId, @Argument Long userId) {
        try {
            boardService.removeBoardAssignee(boardId, userId);
            return "Assignee removed successfully"; // Return a success message
        } catch (Exception e) {
            throw new RuntimeException("Error removing assignee: " + e.getMessage());
        }
    }
}
