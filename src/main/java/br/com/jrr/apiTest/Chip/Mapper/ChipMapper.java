package br.com.jrr.apiTest.Chip.Mapper;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.Chip.DTO.ChipResponseDTO;
import br.com.jrr.apiTest.Chip.Entity.ChipEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChipMapper {
    
    ChipMapper INSTANCE = Mappers.getMapper(ChipMapper.class);

    ChipResponseDTO toResponse(ChipEntity entity);
    Collection<ChipResponseDTO> toResponse(Collection<ChipEntity> entities);
}
