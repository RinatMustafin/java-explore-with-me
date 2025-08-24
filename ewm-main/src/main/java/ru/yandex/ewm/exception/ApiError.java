package ru.yandex.ewm.exception;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Value
@Builder
public class ApiError {
    List<String> errors;
    String message;
    String reason;
    String status;
    String timestamp;

    public static ApiError of(String message, String reason, int httpStatusCode, String httpStatusName) {
        return ApiError.builder()
                .errors(List.of())
                .message(message)
                .reason(reason)
                .status(httpStatusName)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
