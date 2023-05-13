package ru.practicum.shareit.booking.exception;

public class DateEndBeforeStartException extends Exception {
    public DateEndBeforeStartException(String message) {
        super(message);
    }
}
