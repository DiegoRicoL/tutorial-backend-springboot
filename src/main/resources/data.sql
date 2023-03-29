INSERT INTO CATEGORY(id, name) VALUES (1, 'Eurogames');
INSERT INTO CATEGORY(id, name) VALUES (2, 'Ameritrash');
INSERT INTO CATEGORY(id, name) VALUES (3, 'Familiar');

INSERT INTO AUTHOR(id, name, nationality) VALUES (1, 'Alan R. Moon', 'US');
INSERT INTO AUTHOR(id, name, nationality) VALUES (2, 'Vital Lacerda', 'PT');
INSERT INTO AUTHOR(id, name, nationality) VALUES (3, 'Simone Luciani', 'IT');
INSERT INTO AUTHOR(id, name, nationality) VALUES (4, 'Perepau Llistosella', 'ES');
INSERT INTO AUTHOR(id, name, nationality) VALUES (5, 'Michael Kiesling', 'DE');
INSERT INTO AUTHOR(id, name, nationality) VALUES (6, 'Phil Walker-Harding', 'US');

INSERT INTO GAME(id, title, age, category_id, author_id) VALUES (1, 'On Mars', '14', 1, 2);
INSERT INTO GAME(id, title, age, category_id, author_id) VALUES (2, 'Aventureros al tren', '8', 3, 1);
INSERT INTO GAME(id, title, age, category_id, author_id) VALUES (3, '1920: Wall Street', '12', 1, 4);
INSERT INTO GAME(id, title, age, category_id, author_id) VALUES (4, 'Barrage', '14', 1, 3);
INSERT INTO GAME(id, title, age, category_id, author_id) VALUES (5, 'Los viajes de Marco Polo', '12', 1, 3);
INSERT INTO GAME(id, title, age, category_id, author_id) VALUES (6, 'Azul', '8', 3, 5);

INSERT INTO CLIENT(id, name) VALUES (1, 'Diego');
INSERT INTO CLIENT(id, name) VALUES (2, 'César');
INSERT INTO CLIENT(id, name) VALUES (3, 'Sara');

INSERT INTO LOAN(id, client_id, game_id, begin, end) VALUES (1, 1, 1, '2020-01-01', '2020-01-15');
INSERT INTO LOAN(id, client_id, game_id, begin, end) VALUES (2, 1, 2, '2020-01-01', '2020-01-15');
INSERT INTO LOAN(id, client_id, game_id, begin, end) VALUES (3, 3, 1, '2020-01-16', '2020-01-27');
INSERT INTO LOAN(id, client_id, game_id, begin, end) VALUES (4, 2, 3, '2020-02-01', '2020-02-15');
INSERT INTO LOAN(id, client_id, game_id, begin, end) VALUES (5, 3, 4, '2020-06-01', '2020-06-15');
INSERT INTO LOAN(id, client_id, game_id, begin, end) VALUES (6, 2, 3, '2020-02-16', '2020-02-27');
