package com.deili.deilimanagement.card.controller;

import com.deili.deilimanagement.card.dto.*;
import com.deili.deilimanagement.card.service.CardContentService;
import com.deili.deilimanagement.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final CardContentService cardContentService;

    @QueryMapping
    public List<CardDto> getAllCards(){
        return cardService.getAllCards();
    }

    @QueryMapping
    public CardDto getCardById(Long cardId){
        return cardService.getCardById(cardId);
    }

    @QueryMapping
    public List<CardDto> getCardsByLane(Long laneId){
        return cardService.getCardsByLane(laneId);
    }

    @MutationMapping
    public CardDto createCard(CardDto cardDto){
        return cardService.createCard(cardDto);
    }

    @MutationMapping
    public CardDto updateCard(Long cardId, CardDto cardDto){
        return cardService.updateCard(cardId, cardDto);
    }

    @MutationMapping
    public CardDto updateDueDate(Long cardId, String dueDate){
        return cardService.updateDueDate(cardId, LocalDateTime.parse(dueDate));
    }

    @MutationMapping
    public boolean deleteCard(Long cardId){
        cardService.deleteCard(cardId);
        return true;
    }

    @MutationMapping
    public boolean reorderCardsInlane(Long laneId, List<Long> cardIds){
        return cardService.reorderCardsInLane(laneId, cardIds);
    }

    @MutationMapping
    public boolean moveCardToLane(Long cardId, Long targetLaneId, Long newPosition){
        return cardService.moveCardToLane(cardId, targetLaneId, newPosition);
    }

    // CARD CONTENT
    @MutationMapping
    public CardAssigneeDto addAssigneeToCard(Long cardId, Long userId) {
        return cardContentService.addAssigneeToCard(cardId, userId);
    }

    @MutationMapping
    public boolean removeAssigneeFromCard(Long cardAssigneeId) {
        cardContentService.removeAssigneeFromCard(cardAssigneeId);
        return true;
    }

    @QueryMapping
    public List<CardAssigneeDto> getAssigneesByCard(Long cardId) {
        return cardContentService.getAssigneesByCard(cardId);
    }

    @QueryMapping
    public CardAssigneeDto getAssigneeById(Long assigneeId) {
        return cardContentService.getAssigneeById(assigneeId);
    }

    @MutationMapping
    public LabelDto addLabelToCard(Long cardId, String labelName) {
        return cardContentService.addLabelToCard(cardId, labelName);
    }

    @MutationMapping
    public boolean removeLabel(Long labelId) {
        cardContentService.removeLabel(labelId);
        return true;
    }

    @MutationMapping
    public LabelDto updateLabel(Long labelId, String labelName) {
        return cardContentService.updateLabel(labelId, labelName);
    }

    @QueryMapping
    public LabelDto getLabelById(Long labelId) {
        return cardContentService.getLabelById(labelId);
    }

    @QueryMapping
    public List<LabelDto> getLabelsByCard(Long cardId) {
        return cardContentService.getLabelsByCard(cardId);
    }

    @MutationMapping
    public ChecklistDto addChecklistToCard(Long cardId) {
        return cardContentService.addChecklistToCard(cardId);
    }

    @MutationMapping
    public ChecklistItemDto addChecklistItem(Long checklistId, String content) {
        return cardContentService.addChecklistItem(checklistId, content);
    }

    @MutationMapping
    public ChecklistItemDto updateChecklistItem(Long checklistItemId, String content, boolean status) {
        return cardContentService.updateChecklistItem(checklistItemId, content, status);
    }

    @MutationMapping
    public boolean removeChecklistItem(Long checklistItemId) {
        cardContentService.removeChecklistItem(checklistItemId);
        return true;
    }

    @QueryMapping
    public ChecklistDto getChecklistById(Long checklistId) {
        return cardContentService.getChecklistById(checklistId);
    }

    @QueryMapping
    public List<ChecklistDto> getChecklistsByCard(Long cardId) {
        return cardContentService.getChecklistsByCard(cardId);
    }

    @QueryMapping
    public ChecklistItemDto getChecklistItemById(Long checklistItemId) {
        return cardContentService.getChecklistItemById(checklistItemId);
    }
}
