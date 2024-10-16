package br.com.jrr.apiTest.Ticket.DTO;

public record ItemOrderInfo(
    String id,
    String title,
    int quantity,
    String currency_id,
    double unit_price
) {
    
}
