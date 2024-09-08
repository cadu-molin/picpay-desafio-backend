package com.picpaysimplificado.mapstruct.user;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toUserEntity(UserDTO userDTO);

    UserDTO toUserDTO(User user);
}
