CREATE TABLE account
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    email    VARCHAR NOT NULL UNIQUE,
    phone    VARCHAR NOT NULL UNIQUE
);

CREATE TABLE ticket
(
    id         SERIAL PRIMARY KEY,
    session_id INT NOT NULL,
    row        INT NOT NULL,
    cell       INT NOT NULL,
    account_id INT NOT NULL REFERENCES account (id),
    hall_id    INT NOT NULL REFERENCES hall (id)
);

ALTER TABLE ticket
    ADD CONSTRAINT uq_row_cell_session UNIQUE (session_id, row, cell);

CREATE TABLE hall
(
    id          SERIAL PRIMARY KEY,
    total_rows  INT NOT NULL,
    total_cells INT NOT NULL
);

ALTER TABLE hall
    ADD refreshtimeout bigint NOT NULL
        DEFAULT 300000;

CREATE TABLE session
(
    id       SERIAL PRIMARY KEY,
    movie_id INT NOT NULL
);

CREATE TABLE movie
(
    id   SERIAL PRIMARY KEY,
    name text
);

INSERT INTO movie(id, name)
VALUES (1, 'movie');

INSERT INTO session(id, movie_id)
VALUES (1, 1);

INSERT INTO hall(id, total_rows, total_cells)
VALUES (2, 3, 3);

