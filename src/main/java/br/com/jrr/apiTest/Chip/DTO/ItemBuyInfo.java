package br.com.jrr.apiTest.Chip.DTO;

public record ItemBuyInfo(
    String id,
    String title,
    int quantity,
    String currency_id,
    double unit_price
) {
    
}
