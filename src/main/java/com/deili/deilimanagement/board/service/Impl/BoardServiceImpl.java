package com.deili.deilimanagement.board.service.Impl;

import com.deili.deilimanagement.board.dto.AssigneeDto;
import com.deili.deilimanagement.board.dto.BoardDto;
import com.deili.deilimanagement.board.dto.BoardRequestDto;
import com.deili.deilimanagement.board.entity.Board;
import com.deili.deilimanagement.board.entity.BoardAssignee;
import com.deili.deilimanagement.board.entity.enums.BoardRole;
import com.deili.deilimanagement.board.entity.enums.InvitationStatus;
import com.deili.deilimanagement.board.repository.BoardRepository;
import com.deili.deilimanagement.board.service.BoardService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final JavaMailSender mailSender;
    private final ApplicationEventPublisher eventPublisher;

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
        ownerAssignee.setStatus(InvitationStatus.ACCEPTED);
        List<BoardAssignee> assignees = new ArrayList<>();
        assignees.add(ownerAssignee);
        board.setBoardAssignees(assignees);

        boardRepository.save(board);

        return mapToDTO(board);
    }

    @Override
    public BoardDto updateBoard(Long id, BoardRequestDto boardRequestDTO) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id " + id));

        if (boardRequestDTO.getBoardName() != null) board.setBoardName(boardRequestDTO.getBoardName());

        if (boardRequestDTO.getBoardDesc() != null) board.setBoardDesc(boardRequestDTO.getBoardDesc());

        if (boardRequestDTO.isComplete() != board.isComplete()) board.setComplete(boardRequestDTO.isComplete());
        boardRepository.save(board);
        return mapToDTO(board);
    }

    @Override
    public BoardDto toggleBoardCompletion(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id " + id));
        board.setComplete(!board.isComplete());

        boardRepository.save(board);
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
    public List<BoardDto> getBoardByUser(Long userId){
        List<Board> boards = boardRepository.findByUserId(userId);

        return boards.stream().map(this::mapToDTO).collect(Collectors.toList());
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
                .anyMatch(boardAssignee -> boardAssignee.getUser().getId().equals(userId) && boardAssignee.getStatus() == InvitationStatus.ACCEPTED);
        if (isAlreadyAssignee) {
            throw new IllegalArgumentException("User is already an assignee on this board with an accepted invitation");
        }
        Optional<BoardAssignee> existingAssignee = board.getBoardAssignees().stream()
                .filter(boardAssignee -> boardAssignee.getUser().getId().equals(userId))
                .findFirst();
        if (existingAssignee.isPresent()) {
            BoardAssignee assignee = existingAssignee.get();
            if (assignee.getStatus() == InvitationStatus.REJECTED) {
                assignee.setStatus(InvitationStatus.PENDING);
                assignee.setRole(role);
                boardRepository.save(board);
                sendInvitationEmail(user.getEmail(), board.getBoardName(), "http://localhost:3000/notification");
                return;
            }
        }

        BoardAssignee assignee = new BoardAssignee();
        assignee.setBoard(board);
        assignee.setUser(user);
        assignee.setRole(role);
        assignee.setStatus(InvitationStatus.PENDING);

        board.getBoardAssignees().add(assignee);
        boardRepository.save(board);
        sendInvitationEmail(user.getEmail(), board.getBoardName(), "http://localhost:3000/notification");
    }

    private void sendInvitationEmail(String recipientEmail, String boardName, String notificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Project Board Invitation");
        message.setText("You have been invited to join the board: " + boardName + ".\n" +
                "You can accept or reject the invitation by check your notification or clicking the link below:\n" +
                notificationLink);
        mailSender.send(message);
    }

    @Override
    public void respondToInvitation(Long boardId, Long userId, boolean accept) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id " + boardId));

        BoardAssignee assignee = board.getBoardAssignees().stream()
                .filter(a -> a.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No pending invitation found for this user"));
        if (assignee.getStatus() != InvitationStatus.PENDING) throw new IllegalArgumentException("Invitation has already been responded to");

        if (accept) {
            assignee.setStatus(InvitationStatus.ACCEPTED);
        } else {
            assignee.setStatus(InvitationStatus.REJECTED);
        }
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

        List<AssigneeDto> assigneeDtos = board.getBoardAssignees() != null
                ? board.getBoardAssignees().stream()
                .map(assignee -> {
                    AssigneeDto assigneeDto = new AssigneeDto();
                    assigneeDto.setUserId(assignee.getUser().getId());
                    assigneeDto.setUserName(assignee.getUser().getFirstName() + " " + assignee.getUser().getLastName());
                    assigneeDto.setRole(assignee.getRole());
                    assigneeDto.setEmail(assignee.getUser().getEmail());
                    assigneeDto.setJobRole(assignee.getUser().getJobRole());
                    assigneeDto.setStatus(assignee.getStatus() != null ? assignee.getStatus() : InvitationStatus.PENDING);
                    return assigneeDto;
                })
                .collect(Collectors.toList())
                : new ArrayList<>();
        dto.setAssignees(assigneeDtos);
        return dto;
    }
}