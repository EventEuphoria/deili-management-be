package com.deili.deilimanagement.card.service;

import com.deili.deilimanagement.card.dto.CardDto;
import com.deili.deilimanagement.card.entity.Card;

import java.time.LocalDateTime;
import java.util.List;

public interface CardService {
    CardDto createCard(CardDto cardDto);
    CardDto updateCard(Long cardId, CardDto cardDto);
    CardDto updateDueDate(Long cardId, LocalDateTime dueDate);
    void deleteCard(Long cardId);
    List<CardDto> getAllCards();
    CardDto getCardById(Long cardId);
    List<CardDto> getCardsByLane(Long laneId);
    boolean reorderCardsInLane(Long laneId, List<Long> cardIds);
    boolean moveCardToLane(Long cardId, Long targetLaneId, Long newPosition);
}
