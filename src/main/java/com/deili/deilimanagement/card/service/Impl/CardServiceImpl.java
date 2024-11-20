package com.deili.deilimanagement.card.service.Impl;

import com.deili.deilimanagement.card.dto.CardDto;
import com.deili.deilimanagement.card.entity.Card;
import com.deili.deilimanagement.card.repository.CardRepository;
import com.deili.deilimanagement.card.service.CardService;
import com.deili.deilimanagement.exception.ResourceNotFoundException;
import com.deili.deilimanagement.lane.entity.Lane;
import com.deili.deilimanagement.lane.repository.LaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final LaneRepository laneRepository;

    @Override
    public CardDto createCard(CardDto cardDto) {
        Lane lane = laneRepository.findById(cardDto.getLaneId())
                .orElseThrow(() -> new ResourceNotFoundException("Lane not found by id "+cardDto.getLaneId()));
        Card card = new Card();
        card.setCardName(cardDto.getCardName());
        card.setLane(lane);
        card.setPosition((long) lane.getCard().size());

        cardRepository.save(card);
        return mapToDto(card);
    }

    @Override
    public CardDto updateCard(Long cardId, CardDto cardDto) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id "+cardId));
        if (cardDto.getCardName() != null && !cardDto.getCardName().isEmpty()) card.setCardName(cardDto.getCardName());
        if (cardDto.getCardDesc() != null && !cardDto.getCardDesc().isEmpty()) card.setCardDesc(cardDto.getCardDesc());
        cardRepository.save(card);
        return mapToDto(card);
    }

    @Override
    public CardDto updateDueDate(Long cardId, LocalDateTime dueDate) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id "+cardId));
        card.setDueDate(dueDate);
        cardRepository.save(card);
        return mapToDto(card);
    }

    @Override
    public void deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id "+cardId));
        cardRepository.delete(card);
    }

    @Override
    public List<CardDto> getAllCards() {
        return cardRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CardDto getCardById(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id "+cardId));
        return mapToDto(card);
    }

    @Override
    public List<CardDto> getCardsByLane(Long laneId) {
        return cardRepository.findByLaneId(laneId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean reorderCardsInLane(Long laneId, List<Long> cardIds) {
        List<Card> cards = cardRepository.findByLaneId(laneId);
        if (cards.isEmpty()) {
            throw new ResourceNotFoundException("No cards found in lane ID " + laneId);
        }

        Map<Long, Card> cardMap = cards.stream()
                .collect(Collectors.toMap(Card::getId, card -> card));
        for (int i = 0; i < cardIds.size(); i++) {
            Long cardId = cardIds.get(i);
            Card card = cardMap.get(cardId);
            if (card == null) {
                throw new IllegalArgumentException("Invalid card ID: " + cardId);
            }
            card.setPosition((long) i);
        }

        cardRepository.saveAll(cards);
        return true;
    }


    @Override
    public boolean moveCardToLane(Long cardId, Long targetLaneId, Long newPosition) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with ID " + cardId));
        Lane targetLane = laneRepository.findById(targetLaneId)
                .orElseThrow(() -> new ResourceNotFoundException("Lane not found with ID " + targetLaneId));

        card.setLane(targetLane);
        card.setPosition(newPosition);
        cardRepository.save(card);
        return true;
    }

    private CardDto mapToDto(Card card){
        CardDto cardDto = new CardDto();
        cardDto.setId(card.getId());
        cardDto.setCardName(card.getCardName());
        cardDto.setCardDesc(card.getCardDesc());
        cardDto.setDueDate(card.getDueDate());
        cardDto.setPosition(card.getPosition());
        cardDto.setLaneId(card.getLane().getId());
        return cardDto;
    }
}
