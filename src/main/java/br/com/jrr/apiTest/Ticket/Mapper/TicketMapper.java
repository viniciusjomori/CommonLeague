package br.com.jrr.apiTest.Ticket.Mapper;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.Ticket.DTO.TicketResponseDTO;
import br.com.jrr.apiTest.Ticket.Entity.TicketEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {
    
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    TicketResponseDTO toResponse(TicketEntity entity);
    Collection<TicketResponseDTO> toResponse(Collection<TicketEntity> entities);
}
