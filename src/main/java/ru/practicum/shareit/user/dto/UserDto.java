package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDto {

    /**
     * уникальный идентификатор пользователя
     */
    private Long id;

    /**
     * имя или логин пользователя
     */
    @NotBlank
    private String name;

    /**
     * адрес электронной почты
     */
    @NotNull
    @Email
    private String email;
}