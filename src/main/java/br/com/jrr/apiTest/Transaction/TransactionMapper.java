package br.com.jrr.apiTest.Transaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import br.com.jrr.apiTest.Transaction.DTOs.TransactionResponseDTO;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TransactionMapper {
    
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "inventory.chip.id", target = "chipId")
    @Mapping(target = "chipsQty", expression = "java(entity.getType().plus ? entity.getChipsQty() : entity.getChipsQty() * -1)")
    TransactionResponseDTO toResponse(TransactionEntity entity);

    default Page<TransactionResponseDTO> toResponse(Page<TransactionEntity> entities) {
        return entities.map(this::toResponse);
    };

}


