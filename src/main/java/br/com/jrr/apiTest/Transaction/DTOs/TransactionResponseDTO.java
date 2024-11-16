package br.com.jrr.apiTest.Transaction.DTOs;

import java.util.UUID;

import br.com.jrr.apiTest.Transaction.Enum.TransactionStatus;
import br.com.jrr.apiTest.Transaction.Enum.TransactionType;

public record TransactionResponseDTO(
    UUID id,
    TransactionType type,
    TransactionStatus status,
    UUID chipId,
    int chipsQty
) {
    
}
