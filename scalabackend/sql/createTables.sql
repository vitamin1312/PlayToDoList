CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username varchar(20) NOT NULL,
    password varchar(200) NOT NULL
);

CREATE TABLE items (
    item_id SERIAL PRIMARY KEY,
    user_id int4 REFERENCES users(id) ON DELETE CASCADE,
    task_text varchar(2000)
);