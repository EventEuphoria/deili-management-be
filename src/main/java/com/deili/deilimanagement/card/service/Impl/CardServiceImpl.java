package com.deili.deilimanagement.card.service.Impl;

import com.deili.deilimanagement.card.dto.CardDto;
import com.deili.deilimanagement.card.dto.CardInput;
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
    public CardDto createCard(CardInput cardInput) {
        Lane lane = laneRepository.findById(cardInput.getLaneId())
                .orElseThrow(() -> new ResourceNotFoundException("Lane not found by id " + cardInput.getLaneId()));
        Card card = new Card();
        card.setCardName(cardInput.getCardName());
        if (cardInput.getCardDesc() != null) {
            card.setCardDesc(cardInput.getCardDesc());
        }

        int maxPosition = lane.getCard().stream()
                .mapToInt(Card::getPosition)
                .max()
                .orElse(0);
        int newPosition = maxPosition + 1;

        boolean positionConflict = true;
        while (positionConflict) {
            positionConflict = false;
            for (Card existingCard : lane.getCard()) {
                if (existingCard.getPosition() == newPosition) {
                    positionConflict = true;
                    newPosition++;
                    break;
                }
            }
        }

        card.setPosition(newPosition);
        card.setLane(lane);
        cardRepository.save(card);
        return mapToDto(card);
    }

    @Override
    public CardDto updateCard(Long cardId, CardInput cardInput) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id "+cardId));
        if (cardInput.getCardName() != null && !cardInput.getCardName().isEmpty()) card.setCardName(cardInput.getCardName());
        if (cardInput.getCardDesc() != null && !cardInput.getCardDesc().isEmpty()) card.setCardDesc(cardInput.getCardDesc());
        cardRepository.save(card);
        card.setId(card.getId());
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
            card.setPosition(i);
        }

        cardRepository.saveAll(cards);
        return true;
    }


    @Override
    public boolean moveCardToLane(Long cardId, Long targetLaneId, int newPosition) {
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
