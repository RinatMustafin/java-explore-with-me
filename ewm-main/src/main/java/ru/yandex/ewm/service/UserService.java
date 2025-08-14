package ru.yandex.ewm.service;

import ru.yandex.ewm.dto.user.NewUserRequest;
import ru.yandex.ewm.dto.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> get(List<Long> ids, int from, int size);
    UserDto create(NewUserRequest request);
    void delete(long userId);
}