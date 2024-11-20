package com.deili.deilimanagement.card.service;

import com.deili.deilimanagement.card.dto.*;

import java.util.List;

public interface CardContentService {
    CardAssigneeDto addAssigneeToCard(Long cardId, Long userId);
    void removeAssigneeFromCard(Long cardAssigneeId);
    CardAssigneeDto getAssigneeById(Long assigneeId);
    List<CardAssigneeDto> getAssigneesByCard(Long cardId);

    // Labels
    LabelDto addLabelToCard(Long cardId, String labelName);
    void removeLabel(Long labelId);
    LabelDto updateLabel(Long labelId, String labelName);
    LabelDto getLabelById(Long labelId);
    List<LabelDto> getLabelsByCard(Long cardId);

    // Checklist
    ChecklistDto addChecklistToCard(Long cardId);
    ChecklistItemDto addChecklistItem(Long checklistId, String content);
    ChecklistItemDto updateChecklistItem(Long checklistItemId, String content, boolean status);
    void removeChecklistItem(Long checklistItemId);
    ChecklistDto getChecklistById(Long checklistId);
    List<ChecklistDto> getChecklistsByCard(Long cardId);
    ChecklistItemDto getChecklistItemById(Long checklistItemId);

}
