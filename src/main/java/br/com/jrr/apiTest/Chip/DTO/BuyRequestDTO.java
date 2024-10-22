package br.com.jrr.apiTest.Chip.DTO;

import java.util.UUID;

public record BuyRequestDTO(
    UUID inventory_id,
    String title,
    int qtde_ficha,
    double unit_price
) {}