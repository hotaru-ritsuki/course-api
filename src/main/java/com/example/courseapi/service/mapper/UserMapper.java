package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.User;
import com.example.courseapi.dto.UserResponseDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link User} and its DTO {@link UserResponseDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface UserMapper extends EntityMapper<UserResponseDTO, User> {

    UserResponseDTO toDto(User user);

    User toEntity(UserResponseDTO userDTO);

    default User fromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
