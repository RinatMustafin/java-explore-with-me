package ru.yandex.ewm.mapper;

import ru.yandex.ewm.dto.user.NewUserRequest;
import ru.yandex.ewm.dto.user.UserDto;
import ru.yandex.ewm.model.User;

public class UserMapper {
    public static User toEntity(NewUserRequest dto) {
        User u = new User();
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        return u;
    }

    public static UserDto toDto(User u) {
        return new UserDto(u.getId(), u.getName(), u.getEmail());
    }
}
