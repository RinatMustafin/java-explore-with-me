INSERT INTO hits (app, uri, ip, timestamp) VALUES
('ewm-main', '/events/1', '192.168.0.101', NOW()),
('ewm-main', '/categories', '192.168.0.102', NOW() - interval '1 hour');
