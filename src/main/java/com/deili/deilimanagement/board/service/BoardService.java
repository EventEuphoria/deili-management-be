package com.deili.deilimanagement.board.service;

import com.deili.deilimanagement.board.dto.BoardDto;
import com.deili.deilimanagement.board.dto.BoardRequestDto;
import com.deili.deilimanagement.board.entity.enums.BoardRole;

import java.util.List;

public interface BoardService {
    BoardDto createBoard(BoardRequestDto boardRequestDTO);
    BoardDto updateBoard(Long id, BoardRequestDto boardRequestDTO);
    void deleteBoard(Long id);
    List<BoardDto> getAllBoards();
    List<BoardDto> getBoardByUser(Long userId);
    BoardDto getBoardById(Long id);

    BoardDto toggleBoardCompletion(Long id);
    void inviteUserToBoard(Long boardId, Long userId, BoardRole role);
    public void respondToInvitation(Long boardId, Long userId, boolean accept);
    void updateBoardRole(Long boardId, Long userId, BoardRole role);
    void removeBoardAssignee(Long boardId, Long userId);
}
