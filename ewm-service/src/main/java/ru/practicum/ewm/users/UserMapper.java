package ru.practicum.ewm.users;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.users.dto.UserDto;
import ru.practicum.ewm.users.dto.UserDtoPost;

@UtilityClass
public class UserMapper {

    public UserDto mapUserToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        return dto;
    }

    public UserDto mapUserToUserDtoShort(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }

    public User mapUserDtoPostToUser(UserDtoPost dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        return user;
    }

}
