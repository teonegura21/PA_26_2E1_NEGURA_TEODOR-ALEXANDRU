CREATE TABLE genres (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE movies (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    release_date DATE,
    duration INTEGER,
    score DECIMAL(3,1),
    genre_id INTEGER,
    FOREIGN KEY (genre_id) REFERENCES genres(id)
);

CREATE TABLE actors (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    birth_date DATE
);

CREATE TABLE movie_actors (
    movie_id INTEGER,
    actor_id INTEGER,
    PRIMARY KEY (movie_id, actor_id),
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    FOREIGN KEY (actor_id) REFERENCES actors(id) ON DELETE CASCADE
);

CREATE VIEW movie_details_view AS
SELECT 
    m.id AS movie_id,
    m.title,
    m.release_date,
    m.duration,
    m.score,
    g.name AS genre_name,
    a.id AS actor_id,
    a.name AS actor_name
FROM movies m
LEFT JOIN genres g ON m.genre_id = g.id
LEFT JOIN movie_actors ma ON m.id = ma.movie_id
LEFT JOIN actors a ON ma.actor_id = a.id;

INSERT INTO genres (name) VALUES 
    ('Action'),
    ('Drama'),
    ('Comedy'),
    ('Horror'),
    ('Sci-Fi'),
    ('Romance'),
    ('Thriller'),
    ('Animation');

INSERT INTO movies (title, release_date, duration, score, genre_id) VALUES
    ('Inception', '2010-07-16', 148, 8.8, 5),
    ('The Dark Knight', '2008-07-18', 152, 9.0, 1),
    ('Pulp Fiction', '1994-10-14', 154, 8.9, 2),
    ('The Matrix', '1999-03-31', 136, 8.7, 5),
    ('Forrest Gump', '1994-07-06', 142, 8.8, 2);

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
    ('Robin Wright', '1966-04-08');

INSERT INTO movie_actors (movie_id, actor_id) VALUES
    (1, 1), (1, 6),
    (2, 2), (2, 7),
    (3, 3), (3, 8),
    (4, 4), (4, 9),
    (5, 5), (5, 10);
