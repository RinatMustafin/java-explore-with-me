-- Очистка таблиц в правильном порядке (child → parent)
DELETE FROM events;
DELETE FROM users;
DELETE FROM categories;

-- Сброс автоинкрементных последовательностей
ALTER SEQUENCE events_id_seq RESTART WITH 1;
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE categories_id_seq RESTART WITH 1;

-- Вставка категорий с фиксированными id
INSERT INTO categories (id, name) VALUES
(1, 'Концерты'),
(2, 'Фестивали');

-- Вставка пользователей с фиксированными id
INSERT INTO users (id, name, email) VALUES
(1, 'Анна', 'anna@example.com'),
(2, 'Игорь', 'igor@example.com');

-- Вставка событий (foreign keys ссылаются на существующие категории и пользователей)
INSERT INTO events (title, description, event_date, category_id, initiator_id) VALUES
    ('Фестиваль', 'шоу', NOW() + interval '1 day', 2, 1);
