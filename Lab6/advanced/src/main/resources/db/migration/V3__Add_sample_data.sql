INSERT INTO movies (title, release_date, duration, score, genre_id) VALUES
    ('Inception', '2010-07-16', 148, 8.8, 5),
    ('The Dark Knight', '2008-07-18', 152, 9.0, 1),
    ('Pulp Fiction', '1994-10-14', 154, 8.9, 2),
    ('The Matrix', '1999-03-31', 136, 8.7, 5),
    ('Forrest Gump', '1994-07-06', 142, 8.8, 2),
    ('Interstellar', '2014-11-07', 169, 8.6, 5),
    ('The Godfather', '1972-03-24', 175, 9.2, 2),
    ('Fight Club', '1999-10-15', 139, 8.8, 2),
    ('The Avengers', '2012-05-04', 143, 8.0, 1),
    ('Titanic', '1997-12-19', 194, 7.9, 6);

INSERT INTO actors (name, birth_date) VALUES
    ('Leonardo DiCaprio', '1974-11-11'),
    ('Christian Bale', '1974-01-30'),
    ('John Travolta', '1954-02-18'),
    ('Keanu Reeves', '1964-09-02'),
    ('Tom Hanks', '1956-07-09'),
    ('Joseph Gordon-Levitt', '1981-02-17'),
    ('Heath Ledger', '1979-04-04'),
    ('Samuel L. Jackson', '1948-12-21'),
    ('Laurence Fishburne', '1961-07-30'),
    ('Robin Wright', '1966-04-08'),
    ('Al Pacino', '1940-04-25'),
    ('Marlon Brando', '1924-04-03'),
    ('Brad Pitt', '1963-12-18'),
    ('Edward Norton', '1969-08-18'),
    ('Robert Downey Jr.', '1965-04-04'),
    ('Kate Winslet', '1975-10-05'),
    ('Matthew McConaughey', '1969-11-04'),
    ('Anne Hathaway', '1982-11-12');

INSERT INTO movie_actors (movie_id, actor_id) VALUES
    (1, 1), (1, 6), (1, 17),
    (2, 2), (2, 7),
    (3, 3), (3, 8),
    (4, 4), (4, 9),
    (5, 5), (5, 10),
    (6, 17), (6, 18),
    (7, 11), (7, 12),
    (8, 13), (8, 14),
    (9, 15),
    (10, 1), (10, 16);
