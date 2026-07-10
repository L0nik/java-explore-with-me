package ru.practicum.ewm.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDtoPost {

    @NotBlank(message = "Не заполнена электронная почта пользователя")
    @Email(message = "Некорректный формат электронной почты")
    @Size(min = 6, max = 254)
    private String email;

    @NotBlank(message = "Не заполнено имя пользователя")
    @Size(min = 2, max = 250)
    private String name;

}
