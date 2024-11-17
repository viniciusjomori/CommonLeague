package br.com.jrr.apiTest.Chip.Mapper;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.Chip.DTO.ItemInventoryResponseDTO;
import br.com.jrr.apiTest.Chip.Entity.InventoryEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventoryMapper {
    
    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    @Mapping(source = "chip.id", target = "chipId")
    @Mapping(source = "chip.description", target = "description")
    @Mapping(source = "chip.imagePath", target = "imagePath")
    ItemInventoryResponseDTO toResponse(InventoryEntity entity);
    Collection<ItemInventoryResponseDTO> toResponse(Collection<InventoryEntity> entities);
}

