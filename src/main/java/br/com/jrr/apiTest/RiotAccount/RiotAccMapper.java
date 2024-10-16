package br.com.jrr.apiTest.RiotAccount;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.RiotAccount.DTO.RiotAccClientDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RiotAccMapper {

    RiotAccMapper INSTANCE = Mappers.getMapper(RiotAccMapper.class);

    @Mapping(source = "user.id", target = "userId")
    RiotAccClientDTO toResponse(RiotAccEntity entity);
} 
