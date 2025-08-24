package ru.yandex.ewm.model;

public enum EventState {
    PENDING,   // отправлено на модерацию, ждёт публикации
    PUBLISHED, // опубликовано
    CANCELED   // отменено/снято
}
