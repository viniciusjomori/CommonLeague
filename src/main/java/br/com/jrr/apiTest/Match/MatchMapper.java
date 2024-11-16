package br.com.jrr.apiTest.Match;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.Match.DTOs.MatchResponseDTO;
import br.com.jrr.apiTest.Team.Mapper.TeamMapper;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = TeamMapper.class
)
public interface MatchMapper {
    
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mapping(target = "tournamentId", source = "join1.tournament.id")
    @Mapping(target = "teamWinner", source =  "winner.team", qualifiedByName = "toTeamInfo")
    @Mapping(target = "teamLoser", source = "loser.team", qualifiedByName = "toTeamInfo")
    @Mapping(target = "round", source = "loser.round")
    MatchResponseDTO toResponse(MatchEntity entity);

    Collection<MatchResponseDTO> toResponse(Collection<MatchEntity> entities);

}
