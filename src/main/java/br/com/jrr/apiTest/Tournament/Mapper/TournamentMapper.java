package br.com.jrr.apiTest.Tournament.Mapper;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.Team.Mapper.TeamMapper;
import br.com.jrr.apiTest.Tournament.DTOs.TournamentResponseDTO;
import br.com.jrr.apiTest.Tournament.Entity.TournamentEntity;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = TeamMapper.class
)
public interface TournamentMapper {
    
    TournamentJoinMapper INSTANCE = Mappers.getMapper(TournamentJoinMapper.class);

    @Mapping(source = "winner.team", target = "winner", qualifiedByName = "toTeamInfo")
    TournamentResponseDTO toResponse(TournamentEntity entity);

    Collection<TournamentResponseDTO> toResponse(Collection<TournamentEntity> entities);

}

