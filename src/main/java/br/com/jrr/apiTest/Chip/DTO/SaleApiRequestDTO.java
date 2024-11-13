package br.com.jrr.apiTest.Chip.DTO;

public record SaleApiRequestDTO(
    String inventory_id,
    String chave_pix,
    int chips_qty,
    int unit_price
) {
    
}
