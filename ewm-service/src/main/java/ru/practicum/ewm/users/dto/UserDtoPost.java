package ru.practicum.ewm.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDtoPost {

    @NotBlank(message = "Не заполнена электронная почта пользователя")
    @Email(message = "Некорректный формат электронной почты")
    private String email;

    @NotBlank(message = "Не заполнено имя пользователя")
    private String name;

}
