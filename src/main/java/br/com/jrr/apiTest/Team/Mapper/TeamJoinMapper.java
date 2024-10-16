package br.com.jrr.apiTest.Team.Mapper;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.Team.DTO.MemberResponseDTO;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamJoinMapper {
    
    TeamJoinMapper INSTANCE = Mappers.getMapper(TeamJoinMapper.class);

    @Mapping(source = "user.id", target = "userId")
    MemberResponseDTO toMember(TeamJoinEntity entity);

    Collection<MemberResponseDTO> toMember(Collection<TeamJoinEntity> entities);

}
