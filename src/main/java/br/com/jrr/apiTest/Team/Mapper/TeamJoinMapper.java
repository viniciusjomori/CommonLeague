package br.com.jrr.apiTest.Team.Mapper;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.Team.DTO.InviteResponseDTO;
import br.com.jrr.apiTest.Team.DTO.MemberResponseDTO;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.User.UserMapper;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = UserMapper.class
)
public interface TeamJoinMapper {
    
    TeamJoinMapper INSTANCE = Mappers.getMapper(TeamJoinMapper.class);

    @Mapping(source = "joinStatus", target = "status")
    @Mapping(source = "user", target = "user", qualifiedByName = "toUserInfo")
    MemberResponseDTO toMember(TeamJoinEntity entity);

    Collection<MemberResponseDTO> toMember(Collection<TeamJoinEntity> entities);

    @Mapping(source = "id", target = "inviteId")
    @Mapping(source = "team.id", target = "teamId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "joinStatus", target = "status")
    InviteResponseDTO toInvite(TeamJoinEntity entity);

}
