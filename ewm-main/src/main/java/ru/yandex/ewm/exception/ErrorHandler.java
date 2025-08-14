package ru.yandex.ewm.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
public class ErrorHandler {

    // 400 — некорректный запрос/валидация
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError body = ApiError.of(
                e.getMessage(),
                "Некорректный запрос.",
                status.value(),
                status.name()
        );
        return new ResponseEntity<>(body, status);
    }

    // 404 — не найдено
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError body = ApiError.of(
                e.getMessage(),
                "Запрос не найден.",
                status.value(),
                status.name()
        );
        return new ResponseEntity<>(body, status);
    }

    // 409 — конфликт правил/состояний
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        ApiError body = ApiError.of(
                e.getMessage(),
                "Конфликт правил",
                status.value(),
                status.name()
        );
        return new ResponseEntity<>(body, status);
    }

    // 409 — нарушение уникальности/целостности в БД
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        ApiError body = ApiError.of(
                e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : e.getMessage(),
                "Нарушение целостности БД",
                status.value(),
                status.name()
        );
        return new ResponseEntity<>(body, status);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnhandled(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiError body = ApiError.of(
                e.getMessage(),
                "Ошибка",
                status.value(),
                status.name()
        );
        return new ResponseEntity<>(body, status);
    }
}
