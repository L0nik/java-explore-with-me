package ru.practicum.ewm.users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.users.dto.UserDto;
import ru.practicum.ewm.users.dto.UserDtoPost;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public Collection<UserDto> getUsers(Collection<Long> ids, int from, int size) {
        log.info("UserService: получение информации о пользователях");
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids == null) {
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::mapUserToUserDto)
                    .toList();
        } else {
            return userRepository.findByIdIn(ids, pageable).stream()
                    .map(UserMapper::mapUserToUserDto)
                    .toList();
        }
    }

    @Transactional
    public UserDto createUser(@RequestBody @Valid UserDtoPost userData) {
        log.info("UserService: создание нового пользователя");
        User user = UserMapper.mapUserDtoPostToUser(userData);
        userRepository.save(user);
        return UserMapper.mapUserToUserDto(user);
    }

    @Transactional
    public void deleteUser(@PathVariable Long userId) {
        log.info("UserService: удаление пользователя (userId = {})", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%d не найден", userId)));
        userRepository.delete(user);
    }

}
