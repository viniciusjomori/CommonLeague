package br.com.jrr.apiTest.Ticket.DTO;

import java.util.Collection;

public record OrderResponseDTO(
    Collection<ItemOrderInfo> items,
    UrlOrderInfo back_url,
    String notification_url,
    String external_reference,
    boolean expires,
    String expiration_date_from,
    String expiration_date_to,
    String id,
    String init_point,
    String sandbox_init_point
) {
    
}
