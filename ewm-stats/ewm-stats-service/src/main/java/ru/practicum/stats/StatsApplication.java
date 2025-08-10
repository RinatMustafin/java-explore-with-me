package ru.practicum.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatsApplication {

    public static void main(String[] args) {
        System.out.println("DB_URL = " + System.getenv("DB_URL"));
        SpringApplication.run(StatsApplication.class, args);
    }

}
