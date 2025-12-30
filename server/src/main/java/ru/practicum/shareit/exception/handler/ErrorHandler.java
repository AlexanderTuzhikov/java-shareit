package ru.practicum.shareit.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenActionException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final RuntimeException exception) {
        log.error("Запрос на несуществующий ресурс. Error: {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage(), "Ресурс не найден");
    }

    @ExceptionHandler(ForbiddenActionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handlerForbiddenAction(final RuntimeException exception) {
        log.error("Пользователь не является владельцем вещи. Error: {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage(), "Ошибка доступа");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerConflictException(final RuntimeException exception) {
        log.error("Конфликт данных. Error: {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage(), "Конфликт данных");
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final RuntimeException exception) {
        log.error("Ошибка валидации данных. Error: {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage(), "Ошибка валидации данных");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage()
                        + " Получены данные: " + error.getRejectedValue())
                .toList();

        String errorMessage = String.join("; ", errors);
        log.warn("Ошибка валидации: {}", errorMessage);

        return new ErrorResponse(errorMessage, "Ошибка валидации данных");
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Throwable exception) {
        log.error("Внутренняя ошибка сервера. Error: {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage(), "Внутренняя ошибка сервера");
    }
}