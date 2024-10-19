package br.com.jrr.apiTest.Ticket.Mapper;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.Ticket.DTO.ItemInventoryResponseDTO;
import br.com.jrr.apiTest.Ticket.Entity.InventoryEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventoryMapper {
    
    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    @Mapping(source = "ticket.id", target = "ticketId")
    @Mapping(source = "ticket.description", target = "description")
    ItemInventoryResponseDTO toResponse(InventoryEntity entity);
    Collection<ItemInventoryResponseDTO> toResponse(Collection<InventoryEntity> entities);
}

