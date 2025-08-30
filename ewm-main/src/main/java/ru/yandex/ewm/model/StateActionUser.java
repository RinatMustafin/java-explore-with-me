package ru.yandex.ewm.model;

public enum StateActionUser {
    SEND_TO_REVIEW,  // пользователь отправляет на модерацию -> PENDING
    CANCEL_REVIEW    // пользователь отменяет рассмотрение -> CANCELED
}
