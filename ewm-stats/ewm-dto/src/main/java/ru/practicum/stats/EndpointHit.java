package ru.practicum.stats;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndpointHit {
    Long id;
    String app;
    String uri;
    String ip;
    String timestamp; // формат "yyyy-MM-dd HH:mm:ss"
}
