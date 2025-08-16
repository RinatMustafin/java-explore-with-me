package ru.yandex.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.ewm.dto.user.NewUserRequest;
import ru.yandex.ewm.dto.user.UserDto;
import ru.yandex.ewm.exception.NotFoundException;
import ru.yandex.ewm.mapper.UserMapper;
import ru.yandex.ewm.model.User;
import ru.yandex.ewm.repository.UserRepository;
import ru.yandex.ewm.service.UserService;
import ru.yandex.ewm.helper.PageRequestUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    @Override
    public List<UserDto> get(List<Long> ids, int from, int size) {
        Pageable page = PageRequestUtil.of(from, size);
        List<User> users;
        if (ids != null && !ids.isEmpty()) {
            users = repo.findAllByIds(ids, page);
        } else {
            users = repo.findAll(page).getContent();
        }
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto create(NewUserRequest request) {

        repo.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new DataIntegrityViolationException("Email должен быть уникальным");
        });
        User saved = repo.save(UserMapper.toEntity(request));
        return UserMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(long userId) {
        User u = repo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User с id=" + userId + " не найден"));
        repo.delete(u);
    }
}
