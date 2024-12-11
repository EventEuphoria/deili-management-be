package com.deili.deilimanagement.card.controller;

import com.deili.deilimanagement.card.dto.*;
import com.deili.deilimanagement.card.service.CardContentService;
import com.deili.deilimanagement.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.graphql.data.method.annotation.Argument;
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
    public CardDto getCardById(@Argument Long cardId){
        return cardService.getCardById(cardId);
    }

    @QueryMapping
    public List<CardDto> getCardsByLane(@Argument Long laneId){
        return cardService.getCardsByLane(laneId);
    }

    @MutationMapping
    public CardDto createCard(@Argument CardInput cardInput){
        return cardService.createCard(cardInput);
    }

    @MutationMapping
    public CardDto updateCard(@Argument Long cardId, @Argument CardInput cardInput){
        return cardService.updateCard(cardId, cardInput);
    }

    @MutationMapping
    public CardDto updateDueDate(@Argument Long cardId, @Argument String dueDate){
        return cardService.updateDueDate(cardId, LocalDateTime.parse(dueDate));
    }

    @MutationMapping
    public boolean deleteCard(@Argument Long cardId){
        cardService.deleteCard(cardId);
        return true;
    }

    @MutationMapping
    public boolean reorderCardsInlane(@Argument Long laneId, @Argument List<Long> cardIds){
        return cardService.reorderCardsInLane(laneId, cardIds);
    }

    @MutationMapping
    public boolean moveCardToLane(@Argument Long cardId, @Argument Long targetLaneId, @Argument int newPosition){
        return cardService.moveCardToLane(cardId, targetLaneId, newPosition);
    }

    // CARD CONTENT
    @MutationMapping
    public CardAssigneeDto addAssigneeToCard(@Argument Long cardId, @Argument Long userId) {
        return cardContentService.addAssigneeToCard(cardId, userId);
    }

    @MutationMapping
    public boolean removeAssigneeFromCard(@Argument Long cardAssigneeId) {
        cardContentService.removeAssigneeFromCard(cardAssigneeId);
        return true;
    }

    @QueryMapping
    public List<CardAssigneeDto> getAssigneesByCard(@Argument Long cardId) {
        return cardContentService.getAssigneesByCard(cardId);
    }

    @QueryMapping
    public CardAssigneeDto getAssigneeById(@Argument Long assigneeId) {
        return cardContentService.getAssigneeById(assigneeId);
    }

    @MutationMapping
    public LabelDto addLabelToCard(@Argument Long cardId, @Argument String labelName) {
        return cardContentService.addLabelToCard(cardId, labelName);
    }

    @MutationMapping
    public boolean removeLabel(@Argument Long labelId) {
        cardContentService.removeLabel(labelId);
        return true;
    }

    @MutationMapping
    public LabelDto updateLabel(@Argument Long labelId, @Argument String labelName) {
        return cardContentService.updateLabel(labelId, labelName);
    }

    @QueryMapping
    public LabelDto getLabelById(@Argument Long labelId) {
        return cardContentService.getLabelById(labelId);
    }

    @QueryMapping
    public List<LabelDto> getLabelsByCard(@Argument Long cardId) {
        return cardContentService.getLabelsByCard(cardId);
    }

    @MutationMapping
    public ChecklistItemDto addChecklistItem(@Argument Long cardId, @Argument String content) {
        return cardContentService.addChecklistItem(cardId, content);
    }

    @MutationMapping
    public ChecklistItemDto updateChecklistItem(@Argument Long checklistItemId, @Argument String content) {
        return cardContentService.updateChecklistItem(checklistItemId, content);
    }

    @MutationMapping
    public ChecklistItemDto toggleChecklistItem(@Argument Long checklistItemId){
        return cardContentService.toggleChecklistItem(checklistItemId);
    }

    @MutationMapping
    public boolean removeChecklistItem(@Argument Long checklistItemId) {
        cardContentService.removeChecklistItem(checklistItemId);
        return true;
    }

    @QueryMapping
    public ChecklistDto getChecklistById(@Argument Long checklistId) {
        return cardContentService.getChecklistById(checklistId);
    }

    @QueryMapping
    public List<ChecklistDto> getChecklistsByCard(@Argument Long cardId) {
        return cardContentService.getChecklistsByCard(cardId);
    }

    @QueryMapping
    public ChecklistItemDto getChecklistItemById(@Argument Long checklistItemId) {
        return cardContentService.getChecklistItemById(checklistItemId);
    }
}
