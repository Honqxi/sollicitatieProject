CREATE TABLE person (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE fine (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    city VARCHAR(100) NOT NULL,
    amount INT NOT NULL,
    person_id BIGINT,
    CONSTRAINT fk_fine_person
        FOREIGN KEY (person_id)
            REFERENCES person(id));