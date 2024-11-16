package br.com.jrr.apiTest.Tournament.Mapper;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.Team.Mapper.TeamMapper;
import br.com.jrr.apiTest.Tournament.DTOs.TournamentJoinResponseDTO;
import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = TeamMapper.class)
public interface TournamentJoinMapper {
    
    TournamentJoinMapper INSTANCE = Mappers.getMapper(TournamentJoinMapper.class);

    @Mapping(source = "tournament.qntChipsPerPlayer", target = "qntChipsPerPlayer")
    @Mapping(source = "id", target = "joinId")
    @Mapping(source = "tournament.id", target = "tournamentId")
    TournamentJoinResponseDTO toResponse(TournamentJoinEntity entity);

    Collection<TournamentJoinResponseDTO> toResponse(Collection<TournamentJoinEntity> entities);

}