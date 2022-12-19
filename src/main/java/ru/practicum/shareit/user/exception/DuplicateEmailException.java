package ru.practicum.shareit.user.exception;

public class DuplicateEmailException extends Exception {
    public DuplicateEmailException(String s) {
        super(s);
    }
}
