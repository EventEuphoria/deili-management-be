package com.deili.deilimanagement.board.service.Impl;

import com.deili.deilimanagement.board.dto.AssigneeDto;
import com.deili.deilimanagement.board.dto.BoardDto;
import com.deili.deilimanagement.board.dto.BoardRequestDto;
import com.deili.deilimanagement.board.entity.Board;
import com.deili.deilimanagement.board.entity.BoardAssignee;
import com.deili.deilimanagement.board.entity.enums.BoardRole;
import com.deili.deilimanagement.board.repository.BoardRepository;
import com.deili.deilimanagement.board.service.BoardService;
import com.deili.deilimanagement.exception.ResourceNotFoundException;
import com.deili.deilimanagement.user.entity.User;
import com.deili.deilimanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    public BoardDto createBoard(BoardRequestDto boardRequestDTO) {
        User user = userRepository.findById(boardRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + boardRequestDTO.getUserId()));

        Board board = new Board();
        board.setBoardName(boardRequestDTO.getBoardName());
        board.setBoardDesc(boardRequestDTO.getBoardDesc());
        board.setComplete(boardRequestDTO.isComplete());
        board.setUser(user);
        board.setRole(BoardRole.OWNER);

        BoardAssignee ownerAssignee = new BoardAssignee();
        ownerAssignee.setBoard(board);
        ownerAssignee.setUser(user);
        ownerAssignee.setRole(BoardRole.OWNER);

        List<BoardAssignee> assignees = new ArrayList<>();
        assignees.add(ownerAssignee);
        board.setBoardAssignees(assignees);

        boardRepository.save(board);

        return mapToDTO(board);
    }


    @Override
    public BoardDto updateBoard(Long id, BoardRequestDto boardRequestDTO) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id"+id));

        boolean isUpdated = false;
        if (boardRequestDTO.getBoardName() != null && !board.getBoardName().equals(boardRequestDTO.getBoardName())) {
            board.setBoardName(boardRequestDTO.getBoardName());
            isUpdated = true;
        }

        if (boardRequestDTO.getBoardDesc() != null && !board.getBoardDesc().equals(boardRequestDTO.getBoardDesc())) {
            board.setBoardDesc(boardRequestDTO.getBoardDesc());
            isUpdated = true;
        }

        if (boardRequestDTO.isComplete() != board.isComplete()) {
            board.setComplete(boardRequestDTO.isComplete());
            isUpdated = true;
        }

        if (isUpdated) {
            boardRepository.save(board);
        }

        return mapToDTO(board);
    }

    @Override
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id "+id));
        boardRepository.delete(board);
    }

    @Override
    public List<BoardDto> getAllBoards() {
        return boardRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public BoardDto getBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id " + id));
        return mapToDTO(board);
    }

    @Override
    public void inviteUserToBoard(Long boardId, Long userId, BoardRole role) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id " + boardId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        boolean isAlreadyAssignee = board.getBoardAssignees().stream()
                .anyMatch(boardAssignee -> boardAssignee.getUser().getId().equals(userId));

        if (isAlreadyAssignee) {
            throw new IllegalArgumentException("User is already an assignee on this board");
        }

        BoardAssignee assignee = new BoardAssignee();
        assignee.setBoard(board);
        assignee.setUser(user);
        assignee.setRole(role);

        board.getBoardAssignees().add(assignee);
        boardRepository.save(board);
    }

    @Override
    public void updateBoardRole(Long boardId, Long userId, BoardRole role) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id " + boardId));
        BoardAssignee assignee = board.getBoardAssignees().stream()
                .filter(a -> a.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User not assigned to this board"));
        assignee.setRole(role);
        boardRepository.save(board);
    }

    @Override
    public void removeBoardAssignee(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with Id " + boardId));
        long ownerCount = board.getBoardAssignees().stream()
                .filter(boardAssignee -> boardAssignee.getRole() == BoardRole.OWNER)
                .count();
        BoardAssignee assignee = board.getBoardAssignees().stream()
                .filter(a -> a.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User not assign to this board"));
        if(assignee.getRole() == BoardRole.OWNER && ownerCount <= 1){
            throw new IllegalArgumentException("Cannot remove the last owner");
        }

        board.getBoardAssignees().remove(assignee);

        assignee.getUser().getCardAssignees().removeIf(cardAssignee ->
                cardAssignee.getCard().getLane().getBoard().getId().equals(boardId));
        boardRepository.save(board);
    }

    private BoardDto mapToDTO(Board board) {
        BoardDto dto = new BoardDto();
        dto.setId(board.getId());
        dto.setBoardName(board.getBoardName());
        dto.setBoardDesc(board.getBoardDesc());
        dto.setComplete(board.isComplete());

        // Create an empty list if boardAssignees is null or empty
        List<AssigneeDto> assigneeDtos = board.getBoardAssignees() != null
                ? board.getBoardAssignees().stream()
                .map(assignee -> {
                    AssigneeDto assigneeDto = new AssigneeDto();
                    assigneeDto.setUserId(assignee.getUser().getId());
                    assigneeDto.setUserName(assignee.getUser().getFirstName() + " " + assignee.getUser().getLastName());
                    assigneeDto.setRole(assignee.getRole());
                    return assigneeDto;
                })
                .collect(Collectors.toList())
                : new ArrayList<>(); // Empty list instead of null

        dto.setAssignees(assigneeDtos); // Always set a non-null list
        return dto;
    }
}