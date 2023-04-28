package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.User;
import com.example.courseapi.dto.UserRequestDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link User} and its DTO {@link UserRequestDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface UserRequestMapper extends EntityMapper<UserRequestDTO, User> {
    UserRequestDTO toDto(User user);

    User toEntity(UserRequestDTO userDTO);

    default User fromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}