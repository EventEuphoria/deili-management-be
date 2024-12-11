package com.deili.deilimanagement.card.service.Impl;

import com.deili.deilimanagement.board.repository.BoardAssigneeRepository;
import com.deili.deilimanagement.card.dto.*;
import com.deili.deilimanagement.card.entity.*;
import com.deili.deilimanagement.card.repository.*;
import com.deili.deilimanagement.card.service.CardContentService;
import com.deili.deilimanagement.exception.ResourceNotFoundException;
import com.deili.deilimanagement.user.entity.User;
import com.deili.deilimanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardContentServiceImpl implements CardContentService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardAssigneeRepository cardAssigneeRepository;
    private final LabelRepository labelRepository;
    private final LabelItemRepository labelItemRepository;
    private final ChecklistRepository checklistRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final BoardAssigneeRepository boardAssigneeRepository;

    // Card Assignee Methods
    @Override
    @Transactional
    public CardAssigneeDto addAssigneeToCard(Long cardId, Long userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with ID: " + cardId));

        Long boardId = card.getLane().getBoard().getId();
        boolean isBoardAssignee = boardAssigneeRepository.existsByBoardIdAndUserId(boardId, userId);

        if (!isBoardAssignee) {
            throw new IllegalArgumentException("User is not assigned to the board. Cannot add as card assignee.");
        }

        User user = boardAssigneeRepository.findByBoardIdAndUserId(boardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found in board assignees")).getUser();

        CardAssignee cardAssignee = new CardAssignee();
        cardAssignee.setCard(card);
        cardAssignee.setUser(user);

        cardAssigneeRepository.save(cardAssignee);
        return mapToCardAssigneeDto(cardAssignee);
    }

    @Override
    public void removeAssigneeFromCard(Long cardAssigneeId) {
        CardAssignee assignee = cardAssigneeRepository.findById(cardAssigneeId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found with ID: " + cardAssigneeId));
        cardAssigneeRepository.delete(assignee);
    }

    @Override
    public CardAssigneeDto getAssigneeById(Long assigneeId) {
        CardAssignee assignee = cardAssigneeRepository.findById(assigneeId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found with ID: " + assigneeId));
        return mapToCardAssigneeDto(assignee);
    }

    @Override
    public List<CardAssigneeDto> getAssigneesByCard(Long cardId) {
        return cardAssigneeRepository.findByCardId(cardId)
                .stream()
                .map(this::mapToCardAssigneeDto)
                .collect(Collectors.toList());
    }

    // Label Methods
    @Override
    @Transactional
    public LabelDto addLabelToCard(Long cardId, String labelName) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with ID: " + cardId));

        List<Label> existingLabels = labelRepository.findByCardId(cardId);
        Label label;
        if (existingLabels.isEmpty()) {
            label = new Label();
            label.setCard(card);
            labelRepository.save(label);
        } else {
            label = existingLabels.get(0);
        }

        LabelItem labelItem = new LabelItem();
        labelItem.setLabel(label);
        labelItem.setLabelName(labelName);

        label.getLabelItems().add(labelItem);
        labelRepository.save(label);

        return mapToLabelDto(label);
    }

    @Override
    public void removeLabel(Long labelId) {
        LabelItem label = labelItemRepository.findById(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));
        labelItemRepository.delete(label);
    }

    @Override
    public LabelDto updateLabel(Long labelId, String labelName) {
        LabelItem label = labelItemRepository.findById(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));

        if (!label.getLabelName().isEmpty()) label.setLabelName(labelName);
        labelItemRepository.save(label);
        return mapToLabelDto(label.getLabel());
    }

    @Override
    public LabelDto getLabelById(Long labelId) {
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));
        return mapToLabelDto(label);
    }

    @Override
    public List<LabelDto> getLabelsByCard(Long cardId) {
        return labelRepository.findByCardId(cardId).stream()
                .map(this::mapToLabelDto)
                .collect(Collectors.toList());
    }

    @Override
    public LabelDto getLabelItemsById(Long labelItemId){
        LabelItem labelItem = labelItemRepository.findById(labelItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Label Item is not found with ID: " + labelItemId));
        return mapToLabelDto(labelItem.getLabel());
    }

    @Override
    @Transactional
    public ChecklistItemDto addChecklistItem(Long cardId, String content) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with ID: " + cardId));

        List<Checklist> existingChecklist = checklistRepository.findByCardId(cardId);

        Checklist checklist;
        if (existingChecklist.isEmpty()) {
            checklist = new Checklist();
            checklist.setCard(card);
            checklistRepository.save(checklist);
        } else {
            checklist = existingChecklist.get(0);
        }

        ChecklistItem checklistItem = new ChecklistItem();
        checklistItem.setChecklist(checklist);
        checklistItem.setContent(content);
        checklistItem.setStatus(false);

        checklistItemRepository.save(checklistItem);

        return mapToChecklistItemDto(checklistItem);
    }

    @Override
    @Transactional
    public ChecklistItemDto updateChecklistItem(Long checklistItemId, String content) {
        ChecklistItem checklistItem = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist item not found with ID: " + checklistItemId));

        if(!checklistItem.getContent().isEmpty()) checklistItem.setContent(content);

        checklistItemRepository.save(checklistItem);
        return mapToChecklistItemDto(checklistItem);
    }

    @Override
    public ChecklistItemDto toggleChecklistItem(Long checklistItemId){
        ChecklistItem checklistItem = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist item not found with id "+checklistItemId));

        checklistItem.setStatus(!checklistItem.isStatus());
        checklistItemRepository.save(checklistItem);
        return mapToChecklistItemDto(checklistItem);
    }

    @Override
    public void removeChecklistItem(Long checklistItemId) {
        ChecklistItem checklistItem = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist item not found with ID: " + checklistItemId));
        checklistItemRepository.delete(checklistItem);
    }

    @Override
    public ChecklistDto getChecklistById(Long checklistId) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist not found with ID: " + checklistId));
        return mapToChecklistDto(checklist);
    }

    @Override
    public List<ChecklistDto> getChecklistsByCard(Long cardId) {
        return checklistRepository.findByCardId(cardId).stream()
                .map(this::mapToChecklistDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChecklistItemDto getChecklistItemById(Long checklistItemId) {
        ChecklistItem checklistItem = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist item not found with ID: " + checklistItemId));
        return mapToChecklistItemDto(checklistItem);
    }

    // Mapping Methods
    private CardAssigneeDto mapToCardAssigneeDto(CardAssignee cardAssignee) {
        return new CardAssigneeDto(
                cardAssignee.getId(),
                cardAssignee.getCard().getId(),
                cardAssignee.getUser().getId(),
                cardAssignee.getUser().getFirstName() + " "+cardAssignee.getUser().getLastName(),
                cardAssignee.getUser().getEmail(),
                cardAssignee.getUser().getJobRole().getTitle()
        );
    }

    private LabelDto mapToLabelDto(Label label) {
        return new LabelDto(
                label.getId(),
                label.getCard().getId(),
                label.getLabelItems().stream()
                        .map(item -> new LabelItemDto(item.getId(), item.getLabelName()))
                        .collect(Collectors.toList())
        );
    }

    private ChecklistDto mapToChecklistDto(Checklist checklist) {
        return new ChecklistDto(
                checklist.getId(),
                checklist.getCard().getId(),
                checklist.getChecklistItems().stream()
                        .map(this::mapToChecklistItemDto)
                        .collect(Collectors.toList())
        );
    }

    private ChecklistItemDto mapToChecklistItemDto(ChecklistItem checklistItem) {
        return new ChecklistItemDto(
                checklistItem.getId(),
                checklistItem.getChecklist().getId(),
                checklistItem.getContent(),
                checklistItem.isStatus()
        );
    }
}