package br.com.jrr.apiTest.Team.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.Team.DTO.TeamRegisterDTO;
import br.com.jrr.apiTest.Team.DTO.TeamResponseDTO;
import br.com.jrr.apiTest.Team.Entity.TeamEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamMapper {
    
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    TeamEntity toEntity(TeamRegisterDTO dto);

    TeamResponseDTO toResponse(TeamEntity entity);

}
