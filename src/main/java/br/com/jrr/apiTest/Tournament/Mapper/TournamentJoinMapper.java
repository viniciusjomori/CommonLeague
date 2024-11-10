package br.com.jrr.apiTest.Tournament.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.Tournament.DTOs.TournamentJoinResponseDTO;
import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TournamentJoinMapper {
    
    TournamentJoinMapper INSTANCE = Mappers.getMapper(TournamentJoinMapper.class);

    @Mapping(source = "tournament.qntChipsPerPlayer", target = "qntChipsPerPlayer")
    TournamentJoinResponseDTO toResponse(TournamentJoinEntity entity);

}

