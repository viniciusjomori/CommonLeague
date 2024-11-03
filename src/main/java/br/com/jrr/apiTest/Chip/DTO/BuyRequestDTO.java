package br.com.jrr.apiTest.Chip.DTO;

import java.util.UUID;

public record BuyRequestDTO(
    UUID inventory_id,
    String title,
    int chips_qty,
    int unit_price
) {}