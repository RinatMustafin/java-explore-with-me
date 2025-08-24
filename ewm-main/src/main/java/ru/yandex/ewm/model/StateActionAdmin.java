package ru.yandex.ewm.model;

public enum StateActionAdmin {
    PUBLISH_EVENT, // админ публикует -> PUBLISHED
    REJECT_EVENT   // админ отклоняет -> CANCELED
}
