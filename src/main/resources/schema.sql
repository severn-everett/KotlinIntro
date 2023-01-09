DROP TABLE IF EXISTS cat;

CREATE TABLE cat
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)     NOT NULL,
    claw_length DOUBLE PRECISION NOT NULL
);

DROP TABLE IF EXISTS dog;

CREATE TABLE dog
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)     NOT NULL,
    tail_length DOUBLE PRECISION NOT NULL
);

DROP TABLE IF EXISTS horse;

CREATE TABLE horse
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255)     NOT NULL,
    hoof_width DOUBLE PRECISION NOT NULL
);