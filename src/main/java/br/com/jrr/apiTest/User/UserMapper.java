package br.com.jrr.apiTest.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.jrr.apiTest.User.DTO.UserRegisterDTO;
import br.com.jrr.apiTest.User.DTO.UserResponseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    UserEntity toEntity(UserRegisterDTO dto);

    UserResponseDTO toResponse(UserEntity entity);
} 
