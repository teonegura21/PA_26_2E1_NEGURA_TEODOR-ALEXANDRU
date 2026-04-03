CREATE TABLE movie_lists (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    creation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE list_movies (
    list_id INTEGER,
    movie_id INTEGER,
    PRIMARY KEY (list_id, movie_id),
    FOREIGN KEY (list_id) REFERENCES movie_lists(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);
