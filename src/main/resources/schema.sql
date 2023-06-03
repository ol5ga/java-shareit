--DROP TABLE IF EXISTS users CASCADE;
--DROP TABLE IF EXISTS items CASCADE;
--DROP TABLE IF EXISTS bookings CASCADE;
--DROP TABLE IF EXISTS requests CASCADE;
--DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR,
    requestor_id INTEGER REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS items(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR NOT NULL,
    is_available boolean NOT NULL,
    owner_id INTEGER REFERENCES users (id),
    request_id INTEGER REFERENCES requests (id),
    CONSTRAINT pk_item PRIMARY KEY (id),
        FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    item_id INTEGER REFERENCES items (id),
    booker_id INTEGER REFERENCES users (id),
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_user_id_to_user
    FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_item_id_to_item
    FOREIGN KEY (item_id) REFERENCES items (id)

);

CREATE TABLE IF NOT EXISTS comments (
   id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
       text_comment VARCHAR,
       item_id INTEGER REFERENCES items (id),
       author_id INTEGER REFERENCES users (id),
       created TIMESTAMP NOT NULL,
      CONSTRAINT fk_comments_item_id_to_item
    FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_comments_user_id_to_user
    FOREIGN KEY (author_id) REFERENCES users (id)
);