CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
    name VARCHAR(255) NOT NULL ,
    email VARCHAR(512) UNIQUE
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
    description VARCHAR(255) NOT NULL ,
    requestor_id BIGINT NOT NULL REFERENCES users (id),
    created TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
    name VARCHAR(255) NOT NULL ,
    description VARCHAR(512) NOT NULL ,
    is_available BOOLEAN NOT NULL ,
    owner_id BIGINT NOT NULL REFERENCES users (id),
    request_id BIGINT REFERENCES requests (id)
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL ,
    end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL ,
    item_id BIGINT NOT NULL REFERENCES items (id) ,
    booker_id  BIGINT NOT NULL REFERENCES users (id),
    status INT NOT NULL
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
    post_text VARCHAR NOT NULL ,
    item_id BIGINT NOT NULL REFERENCES items (id) ,
    author_id BIGINT NOT NULL REFERENCES users (id) ,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);