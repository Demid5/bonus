-- Заполнение таблицы cards
INSERT INTO cards (client_name, number, balance) VALUES
                                                     ('Иван Иванов', '1234567890123456', 10000.00),
                                                     ('Петр Петров', '1234567890123457', 15000.00),
                                                     ('Сидор Сидоров', '1234567890123458', 20000.00);

-- Заполнение таблицы transactions
-- Предположим, что статусы и типы транзакций уже определены в вашей схеме БД как ENUM
INSERT INTO transactions (amount, card_id, timestamp, status, type) VALUES
                                                                        (500.00, 1, NOW(), 'COMPLETED', 'CREDIT'),
                                                                        (1000.00, 1, NOW(), 'COMPLETED', 'DEBIT'),
                                                                        (200.00, 2, NOW(), 'COMPLETED', 'CREDIT'),
                                                                        (700.00, 3, NOW(), 'COMPLETED', 'REFUND');
