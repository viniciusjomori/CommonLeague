package br.com.jrr.apiTest.Team.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.Team.DTO.TeamInfoDTO;
import br.com.jrr.apiTest.Team.DTO.TeamRegisterDTO;
import br.com.jrr.apiTest.Team.DTO.TeamResponseDTO;
import br.com.jrr.apiTest.Team.Entity.TeamEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamMapper {
    
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    @Mapping(target = "imagePath", expression = "java(dto.profile() != null ? dto.profile().imagePath : null)")
    TeamEntity toEntity(TeamRegisterDTO dto);

    TeamResponseDTO toResponse(TeamEntity entity);

    @Named("toTeamInfo")
    TeamInfoDTO toInfo(TeamEntity entity);

}
