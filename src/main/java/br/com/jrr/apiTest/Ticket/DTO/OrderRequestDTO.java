package br.com.jrr.apiTest.Ticket.DTO;

public record OrderRequestDTO(
    String user_id,
    String id_ficha,
    String title,
    int qtde_ficha,
    double unit_price
) {}