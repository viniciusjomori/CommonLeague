package br.com.jrr.apiTest.User;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import br.com.jrr.apiTest.User.DTO.UserInfo;
import br.com.jrr.apiTest.User.DTO.UserRegisterDTO;
import br.com.jrr.apiTest.User.DTO.UserResponseDTO;
import br.com.jrr.apiTest.User.DTO.UserUpdateDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "imagePath", expression = "java(dto.profile() != null ? dto.profile().imagePath : null)")
    UserEntity toEntity(UserRegisterDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "imagePath", expression = "java(dto.profile() != null ? dto.profile().imagePath : entity.getImagePath())")
    @Mapping(target = "password", ignore = true)
    UserEntity updateUser(UserUpdateDTO dto, @MappingTarget UserEntity entity);

    UserResponseDTO toResponse(UserEntity entity);

    @Named("toUserInfo")
    UserInfo toInfo(UserEntity entity);

    default Page<UserResponseDTO> toResponse(Page<UserEntity> entities) {
        return entities.map(this::toResponse);
    };
} 
