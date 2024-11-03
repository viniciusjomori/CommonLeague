package br.com.jrr.apiTest.Chip.DTO;

import java.util.Collection;

public record BuyResponseDTO(
    Collection<ItemBuyInfo> items,
    UrlBuyInfo back_urls,
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
